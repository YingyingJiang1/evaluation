from typing import List
from dataclasses import dataclass, asdict
import subprocess
import os
import xml.etree.ElementTree as ET
import re
import shutil

from dataset import Data, MethodWrapper
import subprocess
import json
from typing import List
from config import ProjectConfigs, ProjectConfig
from transform import ResultManager
from utils import *
from eval import create_pair_dict
from author_tagger import AuthorIDMap
from path import TEST_DIR

from test_env import DockerBuilder, DokcerInfo
from test_coverage import get_test_coverage_dataset, get_maven_classpath


class TestCoverage:
    def __init__(self, project_path: str):
        self.project_path = project_path
        # dict: test_class_name -> list of MethodWrapper
        self.test_class_to_methods = {}

    def get_all_test_classes(self) -> List[str]:
        return list(self.test_class_to_methods.keys())

    def get_covered_methods(self, test_class: str) -> List[MethodWrapper]:
        return self.test_class_to_methods.get(test_class, [])
    
    def save_to_file(self, filepath: str):
        with open(filepath, "w", encoding="utf-8") as f:
            # asdict 会自动把 dataclass 转换为 dict，嵌套的 dataclass 也支持
            json.dump(asdict(self), f, ensure_ascii=False, indent=2)

    @staticmethod
    def from_dict(data: dict) -> "TestCoverage":
        # 利用 dataclass 的解包，不需要自己挨个写
        methods = {
            k: [MethodWrapper(**m) for m in v]
            for k, v in data.get("test_class_to_methods", {}).items()
        }
        return TestCoverage(
            project_path=data["project_path"],
            test_class_to_methods=methods
        )


class TestResult:
    def __init__(self, transformation_methods):
        self.failed_methods: List[str] = []
        self.successs_methods: List[str] = []
        self.total_methods = 0
        self.failed_count = 0

    def get_pass_rate(self) -> float:
        if self.total_methods == 0:
            return 0.0
        return round((self.total_methods - self.failed_count) / self.total_methods * 100, 2)
    
    
    def add_success_pair(self, pair_id):
        self.successs_methods.append(pair_id)
        
    def add_failure_pair(self, pair_id):
        self.failed_methods.append(pair_id)


@dataclass
class ExecutionResult:
    test_class:str=""
    is_compilable:str=""
    tests:int=0
    failures:int=0
    skipped:int=0
    
    def is_all_test_failed(self):
        return self.tests == self.failures
    
    def is_all_test_skipped(self):
        return self.tests == self.skipped
    
    def has_passed_testcase(self):
        return self.tests > 0 and (self.tests - self.failures - self.skipped) > 0
    
    @staticmethod
    def from_dict(data):
        return TestResult(
        test_class=data.get("test_class", ""),
        is_compilable=data.get("is_compilable", ""),
        tests=int(data.get("tests", 0)),
        failures=int(data.get("failures", 0)),
        skipped=int(data.get("skipped", 0))
    )


class ExecutionResultParser():
    def __init__(self):
        self.pattern_map = {}
        
        self.pattern_map["jedis"] = re.compile(
            r"Tests run:\s*(\d+),\s*Failures:\s*(\d+),\s*Errors:\s*(\d+),\s*Skipped:\s*(\d+)"
        )
    
    def parse_jedis_result(self, stdout, stderr, is_compilable):
        pattern = self.pattern_map["jedis"]
        
        result = ExecutionResult()
        result.is_compilable = is_compilable
        # for line in stdout.splitlines():
        #     m = pattern.search(line)
        #     if m:
        #         tests_run, failures, errors, skipped = map(int, m.groups())
        #         test_result.tests += tests_run
        #         test_result.failures += failures
        #         test_result.failures += errors
        #         test_result.skipped += skipped
        
        tests_match = re.search(r"\[\s*(\d+)\s+tests found", stdout)
        failures_match = re.search(r"\[\s*(\d+)\s+tests failed", stdout)
        skipped_match = re.search(r"\[\s*(\d+)\s+tests skipped", stdout)

        if tests_match:
            result.tests = int(tests_match.group(1))
        if failures_match:
            result.failures = int(failures_match.group(1))
        if skipped_match:
            result.skipped = int(skipped_match.group(1))

        return result
    
    # return the number of failures
    def parse_result(self, project_name, stdout, stderr, is_compilable) -> ExecutionResult:
        if project_name == "jedis":
            return self.parse_jedis_result(stdout, stderr, is_compilable)

class Env:
    
    """
    存储测试环境信息，包括每个项目对应的 Docker 镜像和 Jacoco Agent 路径
    """
    def __init__(self, jacoco_agent_path=""):
        self.jacoco_agent_path = jacoco_agent_path
        # project_path -> docker_image 映射

    def add_docker(self, project_path: str, docker_info: DokcerInfo):
        self.project_docker_map[project_path] = docker_info

    def get_docker_image(self, project_name: str) -> DokcerInfo:
        return f"{ project_name.lower()}-image"
    
    
class ExecutionCache:
    def __init__(self):
        # key: tuple[project_name, full_test_class_name]
        self.execution_cache : dict[tuple[str, str], ExecutionResult] = {}
        self.result_dir = "../test/execution"

    
    def save_execution_cache(self, filepath: str):
            serializable_cache = {
                f"{k[0]}::{k[1]}": v.to_dict()
                for k, v in self.execution_cache.items()
            }
            with open(filepath, "w", encoding="utf-8") as f:
                json.dump(serializable_cache, f, ensure_ascii=False, indent=2)

    def load_execution_cache(self, filepath: str):
        try:
            with open(filepath, "r", encoding="utf-8") as f:
                data = json.load(f)
            self.execution_cache = {
                tuple(k.split("::")): ExecutionResult.from_dict(v)
                for k, v in data.items()
            }
        except FileNotFoundError:
            # 第一次运行时没有缓存文件
            self.execution_cache = {}
            
    def get_execution_result(self, project_name, image_name, test_class) -> ExecutionResult:
        filepath = os.path.join(self.result_dir, project_name)
        if not self.execution_cache[project_name] and os.path.exists(filepath):
            self.load_execution_cache(filepath)
        
        if test_class not in self.execution_cache[project_name]:
            result = subprocess.run(
                ["docker", "run", "--rm"] + [ image_name, "java", test_class],
                capture_output=True,
                text=True
            )
            
            execution_result = ExecutionResultParser().parse_result(project_name, result.stdout, result.stderr)
            self.execution_cache[project_name][test_class] = execution_result
        return self.execution_cache[project_name][test_class]
    

class Tester:
    JUnitLauncher = os.path.join("lib", "junit-platform-console-standalone-1.10.2.jar")
    def __init__(self, project_config:ProjectConfig):
        self.env = Env()
        self.project_config = project_config
        self.execution_cache = {}
        self.test_coverages:dict[str, List[MethodWrapper]] = get_test_coverage_dataset(200, project_config.name, project_config.build_tool)
        self.test_data_dir = os.path.join(TEST_DIR, "test-data")
        self.class_path = None
        
    def get_pairs(self):
        id_map = AuthorIDMap()
        id_map.load()
        author_ids = id_map.get_author_ids(self.project_config.name)
        pair_dict = create_pair_dict(200, ["across-project"])
        return [p for p in pair_dict.values() if p.src_author in author_ids]
        
        
    def run_tests(self, transformation_method, min_target_codes) -> TestResult:
        test_result = TestResult(transformation_method)
        
        # 注意：不要使用project_name, 因为无效
        result_manager = ResultManager(create_transformation_result_jsonl_path(transformation_method, min_target_codes))
        transformation_results = result_manager.get_all_results()
        pair_ids = [r.pair_id for r in self.get_pairs()]
        transformation_results = [r for r in transformation_results if r.pair_id in pair_ids]
        result_dict = {r.pair_id:r.code for r in transformation_results}
        
        for p in self.get_pairs():
            pair_id, src_author, src_id = p.pair_id, p.src_author, p.src_id
                
            for test_class, relevant_methods in self.test_coverages.items():
                target_methods = [m for m in relevant_methods if m.id == src_id]
                if len(target_methods) != 1:
                    continue
                
                method = target_methods[0]
                new_execution = self.execute_test(test_class, [method], {src_id:result_dict[pair_id]})
                old_execution = self.get_original_execution_result(test_class)
                
                r = result_manager.get_result_by_id(pair_id)
                r.compilable = new_execution.is_compilable
                
                if self.is_test_passed(old_execution, new_execution):
                    test_result.add_success_pair(pair_id)
                    r.test_passed = "true"
                else:
                    test_result.add_failure_pair(pair_id)
                    r.test_passed = "false"
            result_manager.update_all()
                    
        result_manager.update_all()
        return test_result
    
    
    def get_original_execution_result(self, test_class) -> ExecutionResult:
        if test_class not in self.execution_cache:
            result = self.execute_test(test_class, [], [])
            self.execution_cache[test_class] = result
        return self.execution_cache[test_class]
    
    
    def get_classpath(self):
        if self.class_path:
            return self.class_path
        if self.project_config.build_tool == "maven":
            self.class_path = get_maven_classpath(self.project_config)
        elif self.project_config.build_tool == "gradle":
            return self.project_config.repo_path
        return self.class_path
    
    def execute_test(self, test_class, relevant_methods: List[MethodWrapper], transformation_results_dict) -> ExecutionResult:
        if os.path.exists(self.test_data_dir):
            shutil.rmtree(self.test_data_dir)
                
        file_to_methods: dict[str, List[MethodWrapper]] = {}
        for method in relevant_methods:
            filepath = method.get_filepath()
            file_to_methods.setdefault(filepath, []).append(method)
        
        is_compilable = True
        #  替换源码，生成新java文件
        for filepath, methods in file_to_methods.items():
            with open(filepath, "r", encoding="utf-8") as f:
                lines = f.readlines()

            # 按行号倒序替换，避免后续范围被破坏
            package = ""
            for method in sorted(methods, key=lambda m: m.get_line_range()[0], reverse=True):
                start_line, end_line = method.get_line_range()
                lines[start_line-1:end_line] = transformation_results_dict[method.id]
                package = method.get_package()
            new_filepath = os.path.join(self.test_data_dir, os.path.basename(filepath))
            os.makedirs(os.path.dirname(new_filepath), exist_ok=True)
            with open(new_filepath, "w", encoding="utf-8") as f:
                f.writelines(lines)

            # 2. 编译临时 .java 文件到临时 class 目录
            compile_command = ["javac", "-d", self.test_data_dir, "-cp", f"{self.project_config.jars};{self.get_classpath()}", new_filepath, "-verbose"]
            # print(" ".join(compile_command))
            result = subprocess.run(compile_command, check=True)
            os.remove(new_filepath)
            if not result.returncode == 0:
                is_compilable = False
                break
        
        if is_compilable:
            # 4. 使用 JUnit Platform ConsoleLauncher 运行测试
            command = ["java" ,
                "-jar", self.JUnitLauncher, 
                "--class-path", f"{self.test_data_dir};{self.get_classpath()}", "--select-class", test_class
            ]
            # print(f"test command:{' '.join(command)}")

            result = subprocess.run(
                command,
                cwd=".",
                capture_output=True,
                text=True
            )
            print(result.stdout)

            # 5. 解析执行结果
            result =  ExecutionResultParser().parse_result(
                self.project_config.name, result.stdout, result.stderr, True
            )

        else:
            result = ExecutionResult(test_class, False)
            
        result.test_class = test_class
        return result
            
    # def execute_test(self, test_class, relevant_methods:List[MethodWrapper], transformation_results_dict) -> ExecutionResult:
    #     file_to_methods: dict[str, List[MethodWrapper]] = {}
    #     for method in relevant_methods:
    #         filepath = method.get_filepath()
    #         file_to_methods.setdefault(filepath, []).append(method)

    #     # 2. 替换源码
    #     for filepath, methods in file_to_methods.items():
    #         with open(filepath, "r", encoding="utf-8") as f:
    #             lines = f.readlines()

    #         # 按行号倒序替换，避免后续范围被破坏
    #         for method in sorted(methods, key=lambda m: m.get_line_range()[0], reverse=True):
    #             start_line, end_line = method.get_line_range()
    #             lines[start_line-1:end_line] = transformation_results_dict[method.id]
    #         new_filepath = os.path.join(self.test_data_dir, os.path.basename(filepath))
    #         with open(new_filepath, "w", encoding="utf-8") as f:
    #             f.writelines(lines)

    #     # 4. 在 Docker 中运行
    #     image = self.env.get_docker_image(self.project_config.name)
    #     volume_args = []
    #     for file in file_to_methods.keys():
    #         app_path = file.replace(self.project_config.repo_path, "/app")
    #         volume_path = os.path.join(self.test_data_dir, os.path.basename(file))
    #         volume_args.append("-v")
    #         volume_args.append(f"{volume_path}:{app_path}")
    #     command = ["docker", "run", "--rm"] + volume_args + [ image, "java", test_class]
    #     result = subprocess.run(
    #         command,
    #         capture_output=True,
    #         text=True
    #     )
    #     print(f"command:{' '.join(command)}")
        
    #     execution_result = ExecutionResultParser().parse_result(self.project_config.name, result.stdout, result.stderr)

    #     return execution_result
    
    def is_test_passed(self, old_execution:ExecutionResult, new_execution:ExecutionResult):
        print(old_execution.__dict__)
        print(new_execution.__dict__)
        return new_execution.is_compilable and new_execution == old_execution
    

    def build_env(self):
        print("Build docker...")
        projects = ProjectConfigs().projects
        
        # 构建环境
        for project in projects:
            docker_info = DockerBuilder.build(project.name)
            self.self.env.add_docker(project.repo_path, docker_info)



    def test(self, methods, min_target_codes):
        
        # 运行所有测试
        for method in methods:
            test_result = self.run_tests(method, min_target_codes)
        
        
        # 输出所有method总的测试通过率
        

if __name__ == "__main__":
    # 示例输入
    methods = [
            "egsi",
            "codebuff",
            "deepseek-r1-0528--free",
            "gpt-4.1",
            # "claude-3.7-sonnet"
        ]
    
    min_target_codes = 200
    tester = Tester(ProjectConfigs().get_project_by_name("jedis"))
    tester.test(methods, min_target_codes)
