from concurrent.futures import ThreadPoolExecutor, as_completed
import threading
from typing import List
from dataclasses import dataclass, asdict
import subprocess
from subprocess import CompletedProcess
import os
import xml.etree.ElementTree as ET
import re
import shutil

from dataset import Data, MethodWrapper, TransformPair
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
    successful:int=0
    
    def is_all_test_failed(self):
        return self.tests == self.failures
    
    def is_all_test_skipped(self):
        return self.tests == self.skipped
    
    def has_passed_testcase(self):
        return self.successful > 0
    
    def merge(self, other):
        self.test_class += ";" + other.test_class
        self.tests += other.tests
        self.failures += other.failures
        self.skipped += other.skipped
        self.successful += other.successful
        
    
    @staticmethod
    def from_dict(data):
        return TestResult(
        test_class=data.get("test_class", ""),
        is_compilable=data.get("is_compilable", ""),
        tests=int(data.get("tests", 0)),
        failures=int(data.get("failures", 0)),
        skipped=int(data.get("skipped", 0)),
        successful=int(data.get("successful", 0)),
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
        for line in stdout.splitlines():
            tests_match = re.search(r"\[\s*(\d+)\s+tests found", line)
            failures_match = re.search(r"\[\s*(\d+)\s+tests failed", line)
            skipped_match = re.search(r"\[\s*(\d+)\s+tests skipped", line)
            successful_match = re.search(r"\[\s*(\d+)\s+tests successful", line)

            if tests_match:
                result.tests += int(tests_match.group(1))
            if failures_match:
                result.failures += int(failures_match.group(1))
            if skipped_match:
                result.skipped += int(skipped_match.group(1))
            if successful_match:
                result.tests += int(successful_match.group(1))
        

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
        self.original_execution_result = None
        self.abs_project_path = os.path.abspath(self.project_config.repo_path)
        
    def test(self, methods, min_target_codes):
        print(f"Testing {self.project_config.name}...")
        self.original_execution_result = self.get_execution_result(None, self.project_config.repo_path)
        self.original_execution_result.is_compilable = True
        print(f"original_execution_result: {self.original_execution_result}")
        # 运行所有测试
        for method in methods:
            test_result = self.run_tests(method, min_target_codes)
        
        
        # 输出所有method总的测试通过率
        
    def get_pairs(self):
        id_map = AuthorIDMap()
        id_map.load()
        author_ids = id_map.get_author_ids(self.project_config.name)
        pair_dict = create_pair_dict(200, ["across-project"])
        return [p for p in pair_dict.values() if p.src_author in author_ids]
    
    def get_pair_group(self, pairs):
        groups = []  # 存放所有合法 group

        for pair in pairs:
            placed = False
            # 遍历已有 group，看是否可以放入
            for group in groups:
                # 如果 group 中没有相同 src_id，就可以加入
                if all(existing.src_id != pair.src_id for existing in group):
                    group.append(pair)
                    placed = True
                    break
            
            # 如果没有合法 group，可以新建一个 group
            if not placed:
                groups.append([pair])
        
        return groups
    
    def run_tests(self, transformation_method, min_target_codes) -> TestResult:
        print(f"Running tests for {transformation_method}")
        pair_groups = self.get_pair_group(self.get_pairs())
        # for group in pair_groups:
        #     print(f"group: {[pair.src_id for pair in group]}")        
        
        result_manager = ResultManager(create_transformation_result_jsonl_path(transformation_method, min_target_codes))
        data = Data(self.project_config)
        
        original_execution_result = self.original_execution_result
        
        for group in pair_groups:
            group = [p for p in group if result_manager.get_result_by_id(p.pair_id).test_passed == ""] # 获取未测试的pair
            self._test_pair_group(result_manager, group, data, original_execution_result)
                    
            result_manager.update_all()
        
    def _inject_to_src(self, result_manager:ResultManager, pairs, data:Data, target_root_dir):
        backups = {} # key:filepath, value:code
        file_to_methods: dict[str, list] = {}  # key: filepath, value: list of (start_line, end_line, new_code)

        # 按 filepath 分类方法
        for p in pairs:
            m = [m for m in data.get_data_list(p.src_author) if m.id == p.src_id][0]
            filepath = m.get_filepath()
            start_line, end_line = m.get_line_range()
            new_code = result_manager.get_result_by_id(p.pair_id).code
            file_to_methods.setdefault(filepath, []).append((start_line, end_line, new_code))

        # 对每个文件一次性处理
        for filepath, methods in file_to_methods.items():
            # print(f"Processing {filepath}")
            # 读取原文件并备份
            with open(filepath, "r", encoding="utf-8") as f:
                original_lines = f.readlines()
            
            # 更新文件路径
            reletive_path = filepath.replace(self.project_config.repo_path, "")
            if reletive_path.startswith('/'):
                reletive_path = reletive_path[1:]
            filepath = os.path.join(target_root_dir, reletive_path)
            
            backups[filepath] = original_lines

            methods = sorted(methods, key=lambda x: x[0])  # 按 start_line 升序
            new_lines = []
            cursor = 0

            for start, end, new_code in methods:
                s_idx, e_idx = start - 1, end
                new_lines.extend(original_lines[cursor:s_idx])
                new_lines.extend(new_code.splitlines(keepends=True))
                cursor = e_idx
            new_lines.extend(original_lines[cursor:])

            # 写回修改后的文件
            with open(filepath, "w", encoding="utf-8", newline="\n") as f:
                f.writelines(new_lines)
                f.flush()
            # print(f"Injected into {filepath}")
            # input("Enter to continue...")
        return backups

    def execute_test(self) -> CompletedProcess:
        pass

class GradleTester(Tester):

    def _test_pair_group(self, result_manager, group, data, original_execution_result):
        # 如果 group 为空，直接返回
        if not group:
            return

        # 一次性注入整个 group
        backups = self._inject_to_src(result_manager, group, data, self.project_config.repo_path)
        ret = self.execute_test()
        self.reset(backups)
        if ret is None:
            print("无法执行测试，检查！")
            exit(0)
        new_execution_result = self.get_execution_result(ret, self.project_config.repo_path)

        if new_execution_result.is_compilable and self.is_test_passed(original_execution_result, new_execution_result):
            # 整个 group 都成功
            for pair in group:
                r = result_manager.get_result_by_id(pair.pair_id)
                r.compilable = True
                r.test_passed = True
        else:
            if len(group) == 1:
                # 缩小到单个 pair，直接标记失败
                r = result_manager.get_result_by_id(group[0].pair_id)
                r.compilable = False
                r.test_passed = False
            else:
                # 把 group 拆成两半，递归检测
                mid = len(group) // 2
                self._test_pair_group(result_manager, group[:mid], data, original_execution_result)
                self._test_pair_group(result_manager, group[mid:], data, original_execution_result)
                
    def execute_test(self) -> CompletedProcess:
        try:
            cmd = [f"{os.path.join(os.path.abspath(self.project_config.repo_path), 'gradlew.bat')}", "test", "--no-daemon", "--rerun-tasks", 
               "-x", "compileTestJava",
               "-x", "spotlessJavaApply",
               "-x", "spotlessCheck"
            ]
            print(f"run: {' '.join(cmd)}")
            ret = subprocess.run(cmd, cwd=os.path.abspath(self.project_config.repo_path), capture_output=True, text=True)
            # print(ret)
            return ret
        except Exception as e:
            print(f"运行测试出错: {e}")
            return None
    
    def get_execution_result(self, ret:CompletedProcess, module_paths) -> ExecutionResult:
        
        result = ExecutionResult()
        result.is_compilable = ret and ret.returncode == 0

        # 测试报告路径: 多模块项目可能有多个模块
        test_report_dirs = []
        for module_path in module_paths:
            for root, dirs, files in os.walk(module_path):
                if os.path.basename(root) == "test-results":
                    if "test" in dirs:
                        test_report_dirs.append(os.path.join(root, "test"))
                    elif "testDebugUnitTest" in dirs:
                        test_report_dirs.append(os.path.join(root, "testDebugUnitTest"))

        # 遍历每个模块的测试报告
        for report_dir in test_report_dirs:
            for file in os.listdir(report_dir):
                if not file.endswith(".xml"):
                    continue
                file_path = os.path.join(report_dir, file)
                try:
                    tree = ET.parse(file_path)
                    root = tree.getroot()
                    # <testsuite> 元素包含测试统计
                    tests = int(root.attrib.get("tests", 0))
                    failures = int(root.attrib.get("failures", 0))
                    errors = int(root.attrib.get("errors", 0))
                    skipped = int(root.attrib.get("skipped", 0))

                    result.tests += tests
                    result.failures += failures + errors
                    result.skipped += skipped
                except Exception as e:
                    print(f"解析 {file_path} 出错: {e}")
                    result.is_compilable = "no"
        return result
        
        
    def reset(self, backups):
        for filepath, code_lines in backups.items():
            with open(filepath, "w", encoding="utf-8") as f:
                f.writelines(code_lines)
                f.flush()
            # print(f"Reset {filepath}!")
            # input("Enter to continue...")


class GradleTesterInDocker(GradleTester):
    def __init__(self, project_config: ProjectConfig, max_worker, modules, workspace="/workspace"):
        super().__init__(project_config)
        self.workspace = workspace
        self.exclude_args = ["-x", "runCheckstyle"]
        self.modules = modules
        self.max_worker = max_worker
        self.project_test_data = os.path.join(self.test_data_dir, self.project_config.name.lower())

    def _thread_wrapper(self, result_manager, group, data, original_execution_result):
        self._test_pair_group(result_manager, group, data, original_execution_result)
        result_manager.update_all()
        
    def _test_pair_group(self, result_manager, group, data, original_execution_result):
        volumes, volume_root = self.init_data()
                
        # 如果 group 为空，直接返回
        if not group:
            return

        # 一次性注入整个 group
        module_dirs = [volumn.split(":")[0] for volumn in volumes]
        backups = self._inject_to_src(result_manager, group, data, volume_root)
        ret = self.execute_test(volumes)
        print(ret)
        self.reset(backups)
        if ret is None:
            print("无法执行测试，检查！")
            return
        new_execution_result = self.get_execution_result(ret, module_dirs)

        if new_execution_result.is_compilable and self.is_test_passed(original_execution_result, new_execution_result):
            # 整个 group 都成功
            for pair in group:
                r = result_manager.get_result_by_id(pair.pair_id)
                r.compilable = True
                r.test_passed = True
        else:
            if len(group) == 1:
                # 缩小到单个 pair，直接标记失败
                r = result_manager.get_result_by_id(group[0].pair_id)
                r.compilable = False
                r.test_passed = False
            else:
                # 把 group 拆成两半，递归检测
                mid = len(group) // 2
                self._test_pair_group(result_manager, group[:mid], data, original_execution_result)
                self._test_pair_group(result_manager, group[mid:], data, original_execution_result)
        
    def run_tests(self, transformation_method, min_target_codes) -> TestResult:
        print(f"Test {transformation_method}: {len(self.get_pairs())} pairs")
        pair_groups = self.get_pair_group(self.get_pairs())
        # for group in pair_groups:
        #     print(f"group: {[pair.src_id for pair in group]}")        
        
        result_manager = ResultManager(create_transformation_result_jsonl_path(transformation_method, min_target_codes))
        data = Data(self.project_config)
        
        original_execution_result = self.original_execution_result
        
        untested_groups = [[p for p in group if result_manager.get_result_by_id(p.pair_id) and result_manager.get_result_by_id(p.pair_id).test_passed == ""] for group in pair_groups]      
        if untested_groups:  
            with ThreadPoolExecutor(max_workers=self.max_worker) as executor:
                for group in untested_groups:   
                    executor.submit(self._thread_wrapper, result_manager,group,  data, original_execution_result)
                
            result_manager.update_all()
        
            shutil.rmtree(self.project_test_data, ignore_errors=True)
        
    def init_data(self):
        id = threading.get_ident()
         # 生成线程独立根目录
        root = os.path.join(self.project_test_data, f"thread_{id}")
        volumes = []
        if os.path.exists(root):
            return root
        
         # 拷贝源码到 app 目录
        for module in self.modules:
            target_dir = os.path.join(root, module)
            shutil.copytree(os.path.join(self.project_config.repo_path, module), target_dir)
            volumes.append(f"{target_dir}:{self.workspace}/{module}")
        
        return volumes, root
        
        
        
    def execute_test(self, volumes) -> CompletedProcess:
        volume_args = []
        for v in volumes:
            volume_args.append("-v")
            volume_args.append(v)
        try:
            id = threading.get_ident()
            workspace = self.workspace
            cmd = [
            "docker", "run", "--rm",
            *volume_args,
            # "-v", f"{os.path.join(self.abs_project_path, '.gradle')}:{workspace}/.gradle",
            "-w", workspace,
            f"{self.project_config.name.lower()}-image",
            "./gradlew", "testDebugUnitTest",
            *self.exclude_args
        ]
            print(f"[Thread-{id}] Running tests: {' '.join(cmd)}")
            ret = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
            # print(ret)
            return ret
        except Exception as e:
            print(f"运行测试出错: {e}")
            return None


class MvnTesterInDokcer(GradleTesterInDocker):
    def __init__(self, project_config: ProjectConfig, max_worker, modules, workspace="/workspace"):
        super().__init__(project_config, max_worker, modules, workspace)
        self.exclude_args = []
        
    def execute_test(self, volumes) -> CompletedProcess:
        volume_args = []
        for v in volumes:
            volume_args.append("-v")
            volume_args.append(v)
        try:
            id = threading.get_ident()
            workspace = self.workspace
            cmd = [
            "docker", "run", "--rm",
            *volume_args,
            "-w", workspace,
            f"{self.project_config.name.lower()}-image",
            *self.exclude_args
        ]
            print(f"[Thread-{id}] Running tests: {' '.join(cmd)}")
            ret = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
            # print(ret)
            return ret
        except Exception as e:
            print(f"运行测试出错: {e}")
            return None
        
    def get_execution_result(self, ret: CompletedProcess, module_paths) -> ExecutionResult:
        """
        解析 Maven 多模块项目的测试结果
        """
        result = ExecutionResult()
        result.is_compilable = ret and ret.returncode == 0

        # Maven 多模块测试报告路径: 每个模块的 target/surefire-reports
        test_report_dirs = []
        for module_path in module_paths:
            for root, dirs, files in os.walk(module_path):
                if os.path.basename(root) == "target":
                    if "surefire-reports" in dirs:
                        test_report_dirs.append(os.path.join(root, "surefire-reports"))

        # 遍历每个模块的测试报告
        for report_dir in test_report_dirs:
            for file in os.listdir(report_dir):
                if not file.endswith(".xml") or not file.startswith("TEST-"):
                    continue
                file_path = os.path.join(report_dir, file)
                try:
                    tree = ET.parse(file_path)
                    root_elem = tree.getroot()
                    # <testsuite> 元素包含测试统计
                    tests = int(root_elem.attrib.get("tests", 0))
                    failures = int(root_elem.attrib.get("failures", 0))
                    errors = int(root_elem.attrib.get("errors", 0))
                    skipped = int(root_elem.attrib.get("skipped", 0))

                    result.tests += tests
                    result.failures += failures + errors
                    result.skipped += skipped
                except Exception as e:
                    print(f"解析 {file_path} 出错: {e}")
                    result.is_compilable = False

        return result
        

def test_arthas(methods, min_target_codes, worker):
    arthas_modules = [
    "web-ui",
    "math-game",
    "common",
    "spy",
    "arthas-vmtool",
    "tunnel-common",
    "tunnel-client",
    "core",
    "agent",
    "client",
    "memorycompiler",
    "boot",
    "arthas-agent-attach",
    "arthas-spring-boot-starter",
    "tunnel-server",
    "testcase",
    "site",
    "packaging",
    "labs"
    # "labs/arthas-grpc-web-proxy",
    # "labs/cluster-management/native-agent",
    # "labs/cluster-management/native-agent-management-web",
    # "labs/cluster-management/native-agent-proxy",
    # "labs/cluster-management/native-agent-common",
    # "labs/arthas-grpc-server"
]
    tester = MvnTesterInDokcer(ProjectConfigs().get_project_by_name("arthas"), worker, arthas_modules)
    tester.test(methods, min_target_codes)
    
    
def test_rxjava(methods, min_target_codes, worker):
    modules = [
   "src"
]
    tester = GradleTesterInDocker(ProjectConfigs().get_project_by_name("RxJava"), worker, modules)
    tester.test(methods, min_target_codes)

def test_stirlingpdf(methods, min_target_codes, worker):
    tester = GradleTester(ProjectConfigs().get_project_by_name("Stirling-PDF"))
    tester.test(methods, min_target_codes)
    
def test_newpipe(methods, min_target_codes, worker):
    tester = GradleTesterInDocker(ProjectConfigs().get_project_by_name("NewPipe"),["app"], worker)
    tester.test(methods, min_target_codes)

# 运行测试之前确保项目已经在本地完成测试，可以通过../docker/run.bat脚本来运行项目测试
if __name__ == "__main__":
    # 示例输入
    methods = [
        "deepseek-r1-0528--free",
            "gpt-4.1",
            "codebuff",
            # "egsi",
            # "claude-3.7-sonnet"
        ]
    
    min_target_codes = 200
    worker = 3

    test_arthas(methods, min_target_codes, worker)
