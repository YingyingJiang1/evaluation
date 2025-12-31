import csv
import os
import socket
import time
from config import ProjectConfigs, ProjectConfig
from transform import ResultManager
from dataset import Data, MethodWrapper, TransformPair
from utils import *
from eval import create_pair_dict, TranformType
from author_tagger import AuthorIDMap
from path import TEST_DIR

from concurrent.futures import ThreadPoolExecutor, as_completed
import threading
import shutil
import subprocess
from subprocess import CompletedProcess
from typing import List
from dataclasses import dataclass, asdict
import xml.etree.ElementTree as ET


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
        
        


class Tester:
    def __init__(self, project_config: ProjectConfig, max_worker,args=[], modules=[], workspace="/workspace"):
        self.project_config = project_config
        self.max_worker = max_worker
        self.workspace = workspace
        self.args = args
        self.modules = modules
        self.thread_id_map = {}
        self.lock = threading.Lock()
        self.test_data_dir = os.path.join(TEST_DIR, "test-data")
        self.project_test_data = os.path.join(self.test_data_dir, self.project_config.name.lower())
        self.original_execution_result = None
        self.abs_project_path = os.path.abspath(self.project_config.repo_path)
        self.only_test_compile = True


    
    def get_pairs(self):
        id_map = AuthorIDMap()
        id_map.load()
        author_ids = id_map.get_author_ids(self.project_config.name)
        pair_dict = create_pair_dict(200, ["across-project"])
        return [p for p in pair_dict.values() if p.src_author in author_ids]
    
        
    def get_test_group(self, result_manager:ResultManager):
        groups = []  # 存放所有合法 group
        pairs = self.get_pairs()
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
        
        # 过滤已经测试过或者无法测试到的pair
        with open("coverage_pairs.txt", "r") as f:
            coveraged_pairs = set(line.strip() for line in f.readlines())
        print(coveraged_pairs)
        untested_groups = []  
        for group in groups:
            target_group = []
            for p in group:
                if self.only_test_compile:
                    r = result_manager.get_result_by_id(p.pair_id)
                    if r and r.compilable == "":
                        target_group.append(p)
                else:
                    if p.pair_id not in coveraged_pairs:
                        continue
                    r = result_manager.get_result_by_id(p.pair_id)
                    if r and r.compilable and r.test_passed == "":
                        target_group.append(p)
            if target_group:
                untested_groups.append(target_group)
        return untested_groups
        
    def test(self, methods, min_target_codes):
        print(f"Testing {self.project_config.name}, line_threshold = {min_target_codes}...")
        if not self.original_execution_result:
            self.original_execution_result = self.get_execution_result_from_build([self.project_config.repo_path])
            self.original_execution_result.is_compilable = True
        print(f"original_execution_result: {self.original_execution_result}")
        
        executor = ThreadPoolExecutor(max_workers=self.max_worker)
        
        # 运行所有测试
        data = Data(self.project_config)
        original_execution_result = self.original_execution_result
        for method in methods:
            result_manager = ResultManager(create_transformation_result_jsonl_path(method, min_target_codes))
            untested_groups = self.get_test_group(result_manager)
            futures = []
            if untested_groups:  
                print(f"Test {method}: {len(self.get_pairs())} pairs")
                for id, group in enumerate(untested_groups, start=1): 
                    self._test_pair_group(result_manager, group, data, original_execution_result)
                    # future = executor.submit(self._test_pair_group, result_manager, group, data, original_execution_result)
                    # future.add_done_callback(_callback)
                    # futures.append(future)
            # 阻塞直到所有任务完成
            for future in as_completed(futures):
                future.result()
            result_manager.update_all()
        
        executor.shutdown(wait=True)
        # shutil.rmtree(self.project_test_data, ignore_errors=True)
    
    def execute_test(self, volumes) -> CompletedProcess:
        try:
            for v in volumes:
                path = v[0]
                cmd = self.args
                print(f"run: {' '.join(cmd)}")
                ret = subprocess.run(cmd, cwd=path, capture_output=True, text=True, shell=True)
                # if ret is None or ret.returncode != 0:
                #     print(ret)
                return ret
        except Exception as e:
            print(f"运行测试出错: {e}")
            return None
        
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
            print(f"Injected into {filepath}")
            # input("Enter to continue...")
        return backups

    def reset(self, backups):
        for filepath, code_lines in backups.items():
            with open(filepath, "w", encoding="utf-8") as f:
                f.writelines(code_lines)
                f.flush()
            # print(f"Reset {filepath}!")
            # input("Enter to continue...")
    
    def _test_pair_group(self, result_manager:ResultManager, group, data, original_execution_result):
        id = self.get_worker_id()
        volumes, volume_root = self.init_data(id)
                
        # 如果 group 为空，直接返回
        if not group:
            return
        method_name = os.path.basename(result_manager.output_file).replace(".jsonl", "")
        print(f"Testing {method_name}'s group of size {len(group)} on worker {id}")

        # 一次性注入整个 group
        module_dirs = [volumn[0] for volumn in volumes]
        backups = self._inject_to_src(result_manager, group, data, volume_root)
        ret = self.execute_test(volumes)
        self.reset(backups)
        if ret is None:
            print("无法执行测试，检查！")
            return
        is_compilable = ret and ret.returncode == 0
        
        # if ret.returncode != 0:
        # print(f"{ret}")

        
        if is_compilable:
            # 整个 group 都成功
            for pair in group:
                r = result_manager.get_result_by_id(pair.pair_id)
                r.compilable = True
            if not self.only_test_compile:
                new_execution_result = self.get_execution_result(ret, module_dirs)
                print(f"new execution result: {new_execution_result}")
                test_passed = self.is_test_passed(original_execution_result, new_execution_result)
                for pair in group:
                    r = result_manager.get_result_by_id(pair.pair_id)
                    r.test_passed = test_passed
        else:
            if len(group) == 1:
                # 缩小到单个 pair，直接标记失败
                r = result_manager.get_result_by_id(group[0].pair_id)
                r.compilable = False
            else:
                if len(group) > 4:
                    # 把 group 拆成两半，递归检测
                    mid = len(group) // 2
                    self._test_pair_group(result_manager, group[:mid], data, original_execution_result)
                    self._test_pair_group(result_manager, group[mid:], data, original_execution_result)
                else:
                    for pair in group:
                        self._test_pair_group(result_manager, [pair], data, original_execution_result)
        result_manager.update_all()


    def get_execution_result(self, ret:CompletedProcess, module_paths:list[str]) -> ExecutionResult:
            
        result = ExecutionResult()
        result.is_compilable = ret and ret.returncode == 0
        if not result.is_compilable:
            return result

        if not self.only_test_compile:
            return self.get_execution_result_from_build(module_paths)
        
    
    def get_execution_result_from_build(self, module_paths:list[str]):
        pass

    def get_worker_id(self):
        id = threading.get_ident()
        with self.lock:
            if id not in self.thread_id_map:
                self.thread_id_map[id] = len(self.thread_id_map) % self.max_worker + 1
            return self.thread_id_map[id]
        
    def init_data(self, id):
        def ignore_node_modules(dir, files):
            return ['node_modules'] if 'node_modules' in files else []
    
         # 生成线程独立根目录
        root = os.path.join(self.project_test_data, f"thread_{id}")
        volumes = []
        if self.modules:
            for module in self.modules:
                target_dir = os.path.join(root, module)
                volumes.append([target_dir, f"{self.workspace}/{module}"])
        else: # 挂载整个项目
            target_dir = os.path.join(root)
            volumes.append([target_dir, self.workspace])
            
        if os.path.exists(root):
            return volumes, root
        
         # 拷贝源码到 workdir
        if self.modules:
            for module in self.modules:
                target_dir = os.path.join(root, module)
                shutil.copytree(os.path.join(self.project_config.repo_path, module), target_dir, ignore=ignore_node_modules)
        else:
            shutil.copytree(self.project_config.repo_path, root, ignore=ignore_node_modules)
                    
        return volumes, root
    
    def is_test_passed(self, original_execution_result:ExecutionResult, new_execution_result:ExecutionResult):
        return new_execution_result.is_compilable and new_execution_result.failures <= original_execution_result.failures
    def only_test_compile(self):
        return "compile" in self.args or "compileJava" in self.args
    
class GradleTester(Tester):        
    def __init__(self, project_config: ProjectConfig, max_worker,args=[], modules=[], workspace="/workspace"):
        super().__init__(project_config, max_worker, args, modules, workspace)
        
    def execute_test(self, volumes) -> CompletedProcess:
        try:
            for v in volumes:
                path = v[0]
                cmd = [f"{os.path.join(path, 'gradlew.bat')}", *self.args[1:]]
                print(f"run: {' '.join(cmd)}")
                ret = subprocess.run(cmd, cwd=path, capture_output=True, text=True)
                if ret is None or ret.returncode != 0:
                    print(ret)
                return ret
        except Exception as e:
            print(f"运行测试出错: {e}")
            return None
        
    def get_execution_result_from_build(self, module_paths:list[str]):
        result = ExecutionResult()
        result.is_compilable = True
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
    
class MvnTester(Tester):
    def __init__(self, project_config: ProjectConfig, max_worker, args=[], modules=[], workspace="/workspace"):
        super().__init__(project_config, max_worker, args, modules, workspace)
        
    def get_execution_result_from_build(self, module_paths:list[str]):
        result = ExecutionResult()
        result.is_compilable = True
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
    
    
def test_stirlingpdf(methods, min_target_codes, worker, args, only_test_compile=True):
    args.extend(["--no-daemon", "-x", "compileTestJava", "-x", "spotlessJavaApply","-x", "spotlessCheck", "-x", "check"])
    tester = GradleTester(ProjectConfigs().get_project_by_name("Stirling-PDF"), worker,args=args, modules=[])
    tester.only_test_compile = only_test_compile
    tester.test(methods, min_target_codes)
    
def test_jedis(methods, min_target_codes, worker, args, only_test_compile=True):
    # 执行：docker run -p 6379:6379 -it redis/redis-stack:latest
    # --- 启动 Redis ---
    def is_redis_running():
        result = subprocess.run(
            ["docker", "ps", "--filter", "name=redis", "--format", "{{.Names}}"],
            capture_output=True, text=True
        )
        return "redis" in result.stdout

    def start_redis():
        if not is_redis_running():
            # 删除已存在的同名容器
            subprocess.run(["docker", "rm", "-f", "redis"], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
            # 启动 Redis 容器
            subprocess.run([
                "docker", "run", "-d", "--name", "redis",
                "-p", "6379:6379",
                "redis/redis-stack:latest"
            ])
            print("Redis container started.")

    def wait_redis_ready(host="localhost", port=6379, timeout=30):
        print("Waiting for Redis to be ready...")
        start_time = time.time()
        while True:
            try:
                with socket.create_connection((host, port), timeout=1):
                    print("Redis is ready!")
                    return
            except (ConnectionRefusedError, OSError):
                if time.time() - start_time > timeout:
                    raise TimeoutError("Redis did not start in time.")
                time.sleep(0.5)

    # 执行启动和等待
    start_redis()
    wait_redis_ready()

    # --- 初始化测试 ---
    args.extend(["-Dmaven.test.failure.ignore=true", "-Dformatter.skip=true"])
    project_config = ProjectConfigs().get_project_by_name("jedis")
    tester = MvnTester(project_config, worker, args)
    tester.only_test_compile = only_test_compile
    tester.test(methods, min_target_codes)

    
def test_newpipe(methods, min_target_codes, worker, args, only_test_compile=True):
    args.extend([ "-x", "runCheckstyle"])
    tester = GradleTester(ProjectConfigs().get_project_by_name("NewPipe"), worker, args=args, modules=[])
    tester.only_test_compile = only_test_compile
    tester.test(methods, min_target_codes)
    
def test_rxjava(methods, min_target_codes, worker, args, only_test_compile=True):
    args.extend(["--no-daemon"])
    tester = GradleTester(ProjectConfigs().get_project_by_name("RxJava"), worker, modules=[])
    tester.only_test_compile = only_test_compile
    tester.test(methods, min_target_codes)
    
    
def output_test_result(line_threshold, methods, output_file):
    # 读取 coverage_pairs.txt
    with open("coverage_pairs.txt", "r", encoding="utf-8") as f:
        coveraged_pairs = [line.strip() for line in f]

    results_summary = []

    # 遍历每种方法
    for m in methods:
        result_manager = ResultManager(create_transformation_result_jsonl_path(m, line_threshold))
        all_results = result_manager.get_all_results()
        all_results = [r for r in all_results if not (r.tranform_type == TranformType.ALIGNED or r.tranform_type == TranformType.REVERSAl)]

        total = len(all_results)
        if total == 0:
            print(f"[WARN] No results for {m} at line_threshold={line_threshold}")
            comp_rate = 0.0
            test_rate = 0.0
        else:
            compilable_count = sum(1 for r in all_results if r.compilable)
            test_passed_count = sum(1 for r in all_results if r.test_passed)

            comp_total = len([1.0 for r in all_results if r.compilable != ""])
            comp_rate = compilable_count / comp_total if comp_total > 0 else 1.0
            test_total = len([1 for r in all_results if r.test_passed != ""])
            test_rate = test_passed_count / test_total if test_total > 0 else 1.0

        results_summary.append({
            "Method": m,
            "Compilation Pass Rate": f"{comp_rate:.2%}",
            "Test Pass Rate": f"{test_rate:.2%}"
        })

    # 写入 CSV 文件
    with open(output_file, "a", newline="", encoding="utf-8") as csvfile:
        writer = csv.writer(csvfile)

        # 写入分隔行
        writer.writerow([f"Line Threshold: {line_threshold}"])
        writer.writerow(["Method", "Compilation Pass Rate", "Test Pass Rate"])

        # 写入结果
        for row in results_summary:
            writer.writerow([row["Method"], row["Compilation Pass Rate"], row["Test Pass Rate"]])

        # 空行分隔不同阈值
        writer.writerow([])


def test_zookeeper(methods, min_target_codes, worker, args, only_test_compile=False):
    args.extend(["-Dmaven.test.failure.ignore=true", "-Dmaven.javadoc.skip=true"])
    project_config = ProjectConfigs().get_project_by_name("zookeeper")
    tester = MvnTester(project_config, worker, args)
    tester.only_test_compile = only_test_compile
    tester.test(methods, min_target_codes)
    
def test_arthas(methods, min_target_codes, worker, args, only_test_compile=False):
    args.extend(["-Dmaven.test.failure.ignore=true"])
    project_config = ProjectConfigs().get_project_by_name("arthas")
    tester = MvnTester(project_config, worker, args)
    tester.only_test_compile = only_test_compile
    tester.test(methods, min_target_codes)
    
def test_compile(methods):
    lines = [200, 400, 800, 1000]
    # line = int(sys.argv[1])
    for line in lines:
        min_target_codes = line
        worker = 2
        
        only_test_compile = True
        test_jedis(methods, min_target_codes, worker, ["mvn", "compile"], only_test_compile)
        test_stirlingpdf(methods, min_target_codes, worker, ["./gradlew", "compileJava"], only_test_compile)
        test_newpipe(methods, min_target_codes, worker, ["./gradlew", "compileDebugJavaWithJavac"], only_test_compile)
        test_rxjava(methods, min_target_codes, worker, ["./gradlew", "compileJava"], only_test_compile)
        test_zookeeper(methods, min_target_codes, worker, ["mvn", "compile"], only_test_compile)
        test_arthas(methods, min_target_codes, worker, ["mvn", "compile"], only_test_compile)
        
        
def test_pass_unittest(methods):
    lines = [200, 400, 800, 1000]
    # line = int(sys.argv[1])
    for line in lines:
        min_target_codes = line
        worker = 1
        
        only_test_compile = False
        test_jedis(methods, min_target_codes, worker, ["mvn", "test"], only_test_compile)
        test_stirlingpdf(methods, min_target_codes, worker, ["./gradlew", "test"], only_test_compile)
        test_newpipe(methods, min_target_codes, worker, ["./gradlew", "testDebugUnitTest"], only_test_compile)
        test_rxjava(methods, min_target_codes, worker, ["./gradlew", "test"], only_test_compile)
        test_zookeeper(methods, min_target_codes, worker, ["mvn", "test"], only_test_compile)
        test_arthas(methods, min_target_codes, worker, ["mvn", "install"], only_test_compile)

def reset_test(methods, project_name):
    author_id_map = AuthorIDMap()
    author_id_map.load()
    pair_dict = create_pair_dict(200, ["across-project"])
    lines = [200, 400, 800, 1000]
    for line in lines:
        for m in methods:
            result_manager = ResultManager(create_transformation_result_jsonl_path(m, line))
            for r in result_manager.get_all_results():
                p = pair_dict[("across-project", r.pair_id)]
                if author_id_map.get_author_project(p.src_author) == project_name:
                    r.compilable = ""
                    r.test_passed = ""
            result_manager.update_all()
            
            
def update_test_passed(methods):
    with open("coverage_pairs.txt", "r") as f:
        coveraged_pairs = set(line.strip() for line in f.readlines())
    lines = [200, 400, 800, 1000]
    for line in lines:
        for m in methods:
            result_manager = ResultManager(create_transformation_result_jsonl_path(m, line))
            for r in result_manager.get_all_results():
                if r.code == "":
                    r.test_passed = ""
                    r.compilable = ""
            result_manager.update_all()

    
# 运行测试之前确保项目已经在本地完成测试，可以通过../docker/run.bat脚本来运行项目测试
if __name__ == "__main__":
    import sys
    # 示例输入
    methods = [
        "deepseek-r1-0528--free",
            "gpt-4.1",
            # "gpt-5",
            "codebuff",
            "egsi",
            # "claude-3.7-sonnet"
        ]
    lines = [200, 400, 800, 1000]
    for line in lines:
        output_test_result(line, methods, os.path.join(EVAL_DIR, "test_result.csv"))
    # test_compile(methods)
    # test_pass_unittest(methods)
    
    # for p in ProjectConfigs().get_all_project_names():
    #     reset_test(methods, p)
    # update_test_passed(methods)