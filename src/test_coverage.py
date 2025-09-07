from typing import List
from dataclasses import dataclass, asdict
import subprocess
import os
import xml.etree.ElementTree as ET

import subprocess
import json
from typing import List, Dict

from dataset import MethodWrapper, Data
from eval import create_pair_dict
from config import ProjectConfigs, ProjectConfig
from myparser import JavaTreeSitterParser

@dataclass
class CoverageResult:
    test_class:str
    coverage_methods:dict[str, List[str]] # 格式为：key: qualified_class, vlaue: [method_name#line_number_of_first_code]
    
    @classmethod
    def from_dict(cls, data: dict):
        return cls(**data)
    
    
def get_maven_classpath(project_config:ProjectConfig):
    project_path = project_config.repo_path
    classes_dir = os.path.join(project_path, "target", "classes")
    test_classes_dir = os.path.join(project_path, "target", "test-classes")
    mvn = r"C:\Program Files\apache-maven-3.9.9\bin\mvn.cmd"
    command = [mvn, "-f", os.path.join(project_path, "pom.xml"), "dependency:build-classpath", "-DincludeScope=test", "-DoutputAbsoluteArtifactFilename=true"]
    # print("Command:", " ".join(command))
    mvn_output = subprocess.run(
        command,
        capture_output=True, text=True
    ).stdout.splitlines()
    deps_classpath = ""
    for i, line in enumerate(mvn_output):
        if line.startswith("[INFO] Dependencies classpath:"):
            if i + 1 < len(mvn_output):
                deps_classpath = mvn_output[i + 1].strip()
            break
    # 设置classpath
    full_classpath = f"{classes_dir};{test_classes_dir}"
    if deps_classpath:
        full_classpath += ";" + deps_classpath
    return full_classpath

class CoverageAnalyzer:
    JacocoLib = os.path.join("lib", "jacoco", "lib")
    AgentJar = os.path.join(JacocoLib, "jacocoagent.jar")
    CliJar = os.path.join(JacocoLib, "jacococli.jar")
    JUnitLauncher = os.path.join("lib", "junit-platform-console-standalone-1.10.2.jar")
    
    def __init__(self, project_config:ProjectConfig):
        self.project_config = project_config
        self.project_path = self.project_config.repo_path
        self.build_tool = self.project_config.build_tool
        project_name = os.path.basename(self.project_path)
        self.jacoco_output_dir = os.path.join("../test/jacoco-report",project_name)
        self.output_path = f"../test/coverages/{project_name}.jsonl"
        pass
    
    def analyze_maven_project(self):
        classes_dir = os.path.join(self.project_path, "target", "classes")
        test_classes_dir = os.path.join(self.project_path, "target", "test-classes")
        exec_file = os.path.join(self.jacoco_output_dir, "jacoco.exec")
        os.makedirs(self.jacoco_output_dir, exist_ok=True)
        
         # 获取 Maven 依赖 classpath
        full_classpath = get_maven_classpath(self.project_config)
            
        coverages = self.run_jacoco(classes_dir, test_classes_dir, exec_file, full_classpath)
                
        self.save_coverages(coverages)
        

    def analyze_gradle_project(self):
        classes_dir = os.path.join(self.project_path, "build", "classes", "java", "main")
        test_classes_dir = os.path.join(self.project_path, "build", "classes", "java", "test")
        exec_file = os.path.join(self.jacoco_output_dir, "jacoco.exec")
        os.makedirs(self.jacoco_output_dir, exist_ok=True)

        # 获取 Gradle 测试运行时依赖
        command = ["gradlew", "-q", "dependencies", "--configuration", "testRuntimeClasspath"]
        print("Command:", " ".join(command))
        gradle_output = subprocess.run(
            command,
            cwd=self.project_path,
            capture_output=True, text=True
        ).stdout.splitlines()

        deps = []
        for line in gradle_output:
            line = line.strip()
            # 一般依赖行是 "group:artifact:version" 或 jar 路径
            if line.endswith(".jar") and os.path.isabs(line):
                deps.append(line)

        deps_classpath = ";".join(deps)

        # 设置 classpath
        full_classpath = f"{classes_dir};{test_classes_dir}"
        if deps_classpath:
            full_classpath += ";" + deps_classpath
            
        coverages = self.run_jacoco(classes_dir, test_classes_dir, exec_file, full_classpath)

        self.save_coverages(coverages)
    
    
    def analyze(self):
        if os.path.exists(self.output_path):
            return self.output_path
        if self.build_tool == "maven":
            self.analyze_maven_project()
        else:
            self.analyze_gradle_project()
        return self.output_path
    
    def run_jacoco(self, classes_dir, test_classes_dir, exec_file, full_classpath) -> list[CoverageResult]:
        coverages = []
        for root, _, files in os.walk(test_classes_dir):
            for f in files:
                if f.endswith(".class"):
                    class_file = os.path.join(root, f)
                    rel_path = os.path.relpath(class_file, test_classes_dir)
                    qualified_name = rel_path.replace(os.sep, ".")[:-6]  # 去掉 .class
                    # 运行jacoco分析测试覆盖
                    java_args = [ 
                                 f"-javaagent:{self.AgentJar}=destfile={exec_file},includes=*", 
                                 "-jar", self.JUnitLauncher, 
                                 "--class-path", full_classpath, "--select-class", 
                                 qualified_name, "--reports-dir", self.jacoco_output_dir 
                                ]
                    subprocess.run(["java"] + java_args, check=False)
                    
                    # 输出xml报告
                    xml_path = os.path.join(self.jacoco_output_dir, "report.xml")
                    report_args = [
                    "-jar", self.CliJar, "report", exec_file,
                    "--classfiles", classes_dir,
                    "--sourcefiles", os.path.join(self.project_path, "src"),
                    "--xml", xml_path
                    ]
                    print("Command:", "java", " ".join(report_args))
                    subprocess.run(["java"] + report_args)
                
                    coverages_methods = self.parse_xml_report(xml_path)
                    if coverages_methods:
                        coverages.append(CoverageResult(test_class=qualified_name, coverage_methods=coverages_methods))
        return coverages


    def save_coverages(self, coverages:list[CoverageResult]):
        os.makedirs(os.path.dirname(self.output_path), exist_ok=True)
        with open(self.output_path, "w", encoding="utf-8") as f:
            for r in coverages:
                json_line = json.dumps(asdict(r), ensure_ascii=False)
                f.write(json_line + "\n")
    
    def parse_xml_report(self, xml_path) -> dict[str, List[str]]:
        tree = ET.parse(xml_path)
        root = tree.getroot()
        
        coverages = {}

        # 遍历每个 class
        for cls in root.findall(".//class"):
            method_nodes = cls.findall("method")
            if not method_nodes:
                continue 
            
            class_name = cls.attrib["name"].replace("/", ".")  # 转换为 Java 风格
            methods = []
            for method in method_nodes:
                counter = method.find("counter[@type='METHOD']")
                if counter is not None and int(counter.attrib.get("covered", "0")) > 0:
                    method_name = method.attrib["name"]
                    line_number = method.attrib["line"]
                    
                    if class_name not in coverages:
                        coverages[class_name] = []
                    coverages[class_name].append(f"{method_name}#{line_number}")
                    
        return coverages

def load_test_coverages(path: str) -> List[CoverageResult]:
    results = []
    with open(path, "r", encoding="utf-8") as f:
        for line in f:
            if not line.strip():
                continue
            data = json.loads(line)
            results.append(CoverageResult.from_dict(data))
    return results

def get_first_line_off(code):
    parser = JavaTreeSitterParser("")
    root = parser.parse_code_str(code)
    # 找方法声明节点
    method_nodes = [child for child in root.children if child.type == "method_declaration"]
    if not method_nodes:
        return -1  # 没找到方法

    method_node = method_nodes[0]

    # 找方法体 block
    block_node = None
    for child in method_node.children:
        if child.type == "block":
            block_node = child
            break
    if block_node is None:
        return -1

    # 找 block 内第一个语句
    first_stmt = None
    for child in block_node.children:
        if child.type != "{" and child.type != "}":
            first_stmt = child
            break
    if first_stmt is None:
        return -1

    # 计算偏移
    method_start_line = method_node.start_point[0]  # 方法起始行
    first_stmt_line = first_stmt.start_point[0]     # 第一行代码
    return first_stmt_line - method_start_line

def get_test_coverage_dataset(min_target_codelines, project_name, build_tool, across_project=True) -> Dict[tuple[str, str], List[str]]:
    analyzer = CoverageAnalyzer(ProjectConfigs().get_project_by_name(project_name))
    test_coverage_path =analyzer.analyze()
    
    # 获取所有相关的方法
    if across_project:
        pair_dict = create_pair_dict(min_target_codelines, ["across-project"])
    else:
        pair_dict = create_pair_dict(min_target_codelines, [project_name])
        
    method_map = {}
    project_methods_cache: Dict[str, Data] = {}
    for p in pair_dict.values():
        author, src_id = p.src_author, p.src_id
        if project_name not in project_methods_cache:
            project_methods_cache[project_name] = Data(ProjectConfigs().get_project_by_name(project_name))
        method_data = project_methods_cache[project_name]
        if method_data.get_data_list(author) is None:
            continue
        method = [m for m in method_data.get_data_list(author) if m.id == src_id][0]
        method_name, first_code_line = method.get_method_name(), method.get_line_range()[0] + get_first_line_off(method.get_code())
        top_class = os.path.basename(method.get_filepath()).replace(".java", "")
        top_class = f"{method.get_package()}.{top_class}"
        method_map[f"{top_class}#{method_name}#{first_code_line}"] = method
        
    result = {} # key: test_class, value:[instances of MethodWrapper]
    test_coverages = load_test_coverages(test_coverage_path)
    for coverages in test_coverages:
        for qualified_class, method_info_list in coverages.coverage_methods.items():
            for method_info in method_info_list:
                for key in method_map:
                    strs = key.split("#")
                    if strs[0] in qualified_class and method_info == "#".join(strs[1:]):
                        if coverages.test_class not in result:
                            result[coverages.test_class] = []
                        result[coverages.test_class].append(method_map[key])
    # 修改map：key为(), value为list of test classes
    # method_to_test_classes = {}
    # for test_class, methods in result.items():
    #     for m in methods:
    #         key = (m.get_dominant_author(), m.get_id())
    #         if key not in method_to_test_classes:
    #             method_to_test_classes[key] = []
    #         method_to_test_classes[key].append(test_class)
    return result
        

def analyze_coverages():
    analyzer = CoverageAnalyzer(ProjectConfigs().get_project_by_name("Stirling-PDF"))
    analyzer.analyze()
    
    
    


if __name__== "__main__":
    analyze_coverages()
    
    