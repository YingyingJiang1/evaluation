import os
import subprocess
import concurrent.futures
import json

import xml.etree.ElementTree as ET
import re
import platform

from dataclasses import asdict, dataclass,field 
from collections import Counter

from config import ProjectConfig, ProjectConfigs
from dataset import (
    TransformationData, MethodWrapper, MethodTagResult,
    Data
)
from utils import *
from transform import (
    load_pairs, TransformPairPack, ResultManager,TransformReuslt
)

from tree_sitter import Language
import tree_sitter_java


system = platform.system()
if system == 'Windows':
    lib_ext = '.dll'
else:
    lib_ext = '.so'

lib_path = os.path.join('build', f'my-languages{lib_ext}')
if not os.path.exists(lib_path):
    Language.build_library(
        lib_path,
        ['lib/tree-sitter-java']
    )
JAVA_LANGUAGE = Language(lib_path, 'java')
# JAVA_LANGUAGE = Language(tree_sitter_java.language())

@dataclass
class DokcerInfo:
    success:bool
    image_name:str
    build_tool:str
    


class DockerBuilder:
    @staticmethod
    def parse_java_version_from_pom(pom_path):
        tree = ET.parse(pom_path)
        root = tree.getroot()
        ns = {'maven': 'http://maven.apache.org/POM/4.0.0'}

        # 尝试获取 <maven.compiler.source> 或 <java.version>
        properties = root.find('maven:properties', ns)
        if properties is not None:
            for tag in ['java.version', 'maven.compiler.source']:
                elem = properties.find(f'maven:{tag}', ns)
                if elem is not None:
                    return elem.text.strip()
        return "17"  # 默认 Java 17

    @staticmethod
    def detect_build_tool(project_path):
        if os.path.exists(os.path.join(project_path, 'pom.xml')):
            return 'maven', '3.9'
        elif os.path.exists(os.path.join(project_path, 'build.gradle')):
            return 'gradle', '7.6'
        else:
            return 'unknown', 'unknown'

    @staticmethod
    def generate_dockerfile(project:ProjectConfig, output_dir="../docker"):
        
        project_name = project.name
        project_path = project.repo_path
        build_tool, build_version = DockerBuilder.detect_build_tool(project_path)
        
        os.makedirs(output_dir, exist_ok=True)
        dockerfile_path = os.path.join(output_dir, f'{project_name.lower()}.Dockerfile')
        if os.path.exists(dockerfile_path):
            return build_tool, dockerfile_path

        if build_tool == 'unknown':
            raise Exception("无法识别构建工具：未找到 pom.xml 或 build.gradle")

        java_version = "17"  # 默认 Java 版本
        if build_tool == 'maven':
            pom_path = os.path.join(project_path, 'pom.xml')
            java_version = DockerBuilder.parse_java_version_from_pom(pom_path)
            build_image = f"maven:3.9.9-eclipse-temurin-{java_version}"
        elif build_tool == 'gradle':
            build_image = f"gradle:7.6.4-jdk{java_version}"

        # Dockerfile 模板：支持测试运行
        if build_tool == 'maven':
            dockerfile_content = f"""
    # 构建阶段
    FROM {build_image} AS base
    WORKDIR /app
    
    # 下载maven依赖
    COPY pom.xml ./
    RUN mvn dependency:go-offline -B

    # 复制源码构建项目
    COPY . .

    RUN mvn test-compile -B
    """.strip()

        elif build_tool == 'gradle':
            dockerfile_content = f"""
    # 缓存依赖阶段
    FROM {build_image} AS deps
    WORKDIR /app
    COPY build.gradle settings.gradle gradle.properties ./
    RUN gradle --no-daemon dependencies

    # 构建阶段
    FROM {build_image} AS build
    WORKDIR /app
    COPY . .
    RUN ./gradlew build -x test

    # 运行阶段
    FROM {build_image}
    WORKDIR /app
    COPY --from=build /app .
    """.strip()
    
        with open(dockerfile_path, 'w') as f:
            f.write(dockerfile_content)

        print(f"Dockerfile 已生成：{dockerfile_path}")
        return build_tool, dockerfile_path
        
    @staticmethod
    def build(project_name, skip_build=False) -> DokcerInfo:
        image_name = f"{project_name.lower()}-image"
        try:
            config = ProjectConfigs().get_project_by_name(project_name)
            # 生成 Dockerfile
            build_tool, dockerfile = DockerBuilder.generate_dockerfile(config)

            # 执行构建命令
            if not skip_build:
                cmd = [
                    "docker", "build",
                    "-f", dockerfile,
                    "-t", image_name,
                    config.repo_path
                ]
                print(f"Building {image_name}: {' '.join(cmd)}")
                result = subprocess.run(cmd, check=True)
        except Exception as e:
            print(f"[ERROR] Failed to build '{project_name}': {e}")
            return DokcerInfo(False,None, build_tool)
        
        return DokcerInfo(True, image_name, build_tool)
    



# class TestResultParser():
#     def __init__(self):
#         self.pattern_map = {}
        
#         self.pattern_map["jedis"] = re.compile(
#             r"Tests run:\s*(\d+),\s*Failures:\s*(\d+),\s*Errors:\s*(\d+),\s*Skipped:\s*(\d+)"
#         )
    
#     def parse_jedis_result(self, stdout, stderr):
#         pattern = self.pattern_map["jedis"]
        
#         test_result = TestResult()
#         for line in stdout.splitlines():
#             m = pattern.search(line)
#             if m:
#                 tests_run, failures, errors, skipped = map(int, m.groups())
#                 test_result.tests += tests_run
#                 test_result.failures += failures
#                 test_result.failures += errors
#                 test_result.skipped += skipped
#         return test_result
    
#     # return the number of failures
#     def parse_result(self, project_name, stdout, stderr) -> TestResult:
#         if project_name == "jedis":
#             return self.parse_jedis_result(stdout, stderr)



# class Tester:
#     def __init__(self, transformation_method, min_target_lines):
#         self.transformation_method = transformation_method
#         result_path = create_transformation_result_jsonl_path(method, min_target_lines)
#         self.result_manager = ResultManager(result_path)
#         self.cache = {}
        
#     @staticmethod
#     def run_test_class(project_name, test_class, test_result_parser:TestResultParser, skip_build=False)-> TestResult:
#         is_compilable, docker_image, build_tool = DockerBuilder.build(project_name, skip_build)
#         test_result = TestResult(test_class, is_compilable=is_compilable)
        
#         if not is_compilable:
#             return test_result
        
#         try:
#             if build_tool == "maven":
#                 cmd = ["docker", "run", docker_image, "mvn", "test", f"-Dtest={test_class}"]
#             elif build_tool == "gradle":
#                 cmd = ["docker", "run", docker_image, "./gradlew", "test", f"--tests={test_class}"]
#             else:
#                 print(f"[Warning] Unknown build tool: {build_tool}")
#                 cmd = []

#             if cmd:
#                 print(f"Running cmd: {' '.join(cmd)}\n")
#                 result = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
#                 test_result = test_result_parser.parse_result(project_name, result.stdout, result.stderr)
#                 print(f"return code:{result.returncode}")
#         except Exception as e:
#             print(f"[Error]Failed while testing...:{e}")
            
#         test_result.test_class = test_class
#         test_result.is_compilable = True
#         return test_result
    
    
#     def load_origin_test_result(self, project_name) -> list[TestResult]:
#         results = []
#         path = create_test_result_path(project_name)  
#         with open(path , 'r', encoding='utf-8') as f:
#             for line in f:
#                 if line.strip():  # 跳过空行
#                     data = json.loads(line)
#                     result = TestResult.from_dict(data)
#                     results.append(result)
#         return results
    
    
   
#     def test(self):
#         new_result_manager = ResultManager(self.result_manager.output_file.replace(".jsonl", "-test.jsonl"), 1)
#         all_results = self.result_manager.get_all_results()
#         test_result_parser = TestResultParser()
        
#         # test project before modification
#         original_test_result = {}
#         test_class_result_dict = {}
#         for r in all_results:
#             project_name = r.project_name
#             info_in_project = self.__get_info_in_project(r)
#             test_class = info_in_project.get_test_class()
#             if test_class not in test_class_result_dict:
#                 test_results = self.load_origin_test_result(r.project_name)
#                 for tr in test_results:
#                     test_class_result_dict[tr.test_class] = tr
#                 # test_result = Tester.run_test_class(project_name, test_class, test_result_parser, True)
#                 # test_class_result_dict[test_class] = test_result
            
#             original_test_result[r.pair_id] = test_class_result_dict[test_class]
            
#         # full_failures = []
#         # for r in original_test_result.values():
#         #     print(r.__dict__)
#         #     if r.tests == r.failures:
#         #         full_failures.append(r)
#         # print("---------------------------------------------")
#         # print(f"full failures:{len(full_failures)}")
#         # print("---------------------------------------------")
        
#             # run test class
#         invlaid_pair_count = 0
#         for r in all_results:
#             project_name = r.project_name
#             info_in_project = self.__get_info_in_project(r)
#             test_class = info_in_project.get_test_class()
            
#             if not original_test_result[r.pair_id].has_passed_testcase():
#                 invlaid_pair_count += 1
#                 continue
                        
#             old_code = self.__inject_method(info_in_project, r.code)
#             test_result = Tester.run_test_class(project_name, test_class, test_result_parser, False)
            
#             # 恢复文件
#             self.__inject_method(info_in_project, old_code)
            
#             r.compilable = test_result.is_compilable
#             r.test_passed = test_result.failures <= original_test_result[r.pair_id].failures
                
#             new_result_manager.add_results([r])
#             # break

#         print("-------------------------------------")
#         print(f"invalid pair count:{invlaid_pair_count}")
#         print("-------------------------------------")

#         new_result_manager.flush_all()
#         self.result_manager.update_all()
        
        

    
    
    
#     def __get_info_in_project(self, result:TransformReuslt) -> MethodWrapper:
#         key = (result.project_name, result.src_id)
        
#         if not key in self.cache:
#             project_name = result.project_name
#             config = ProjectConfigs().get_project_by_name(project_name)
#             data = Data(config)
#             lists = list(data.transformation_data.values())
#             lists.extend(list(data.training_data.values()))
#             data_list = []
#             for l in lists:
#                 data_list.extend(l)
#             for d in data_list:
#                 self.cache[(project_name, d.id)] = d
           
            
#         return self.cache[key]



        