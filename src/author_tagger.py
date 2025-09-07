import pandas as pd
import ast
import os
import re
import csv
import subprocess
import random
import os
import json

from collections import defaultdict
from dataclasses import dataclass, asdict
from typing import List, Dict, Tuple

from config import ProjectConfig, ProjectConfigs
from myparser import JavaTreeSitterParser, Method
from utils import *

import os,sys
os.chdir(sys.path[0])

test_classes = set()


@dataclass(frozen=True)
class AuthorID:
    project_name: str
    author_name: str
    author_id: str
    
    


class AuthorIDMap:
    def __init__(self):
        self.output_path = create_author_id_map_path()
        self.author_ids = None
        self._id_lookup: dict[tuple[str, str], str] = {}
        self._name_lookup: dict[str, tuple[str, str]] = {}
        self.id_start = 1
        
    def load(self, path=None):
        if not path:
            path = self.output_path
        if not os.path.exists(path):
            return
        
        with open(path, 'r', encoding='utf-8') as f:
            for line in f:
                item = json.loads(line)
                author = AuthorID(**item)
                self._id_lookup[(author.project_name, author.author_name)] = author.author_id
                self._name_lookup[author.author_id] = (author.project_name, author.author_name)
        self.id_start = max([int("".join(id[1:])) for id in self._name_lookup.keys()]) + 1
            
    def save(self, output_path):
        with open(output_path, 'w', encoding='utf-8') as f:
            for (project_name, author_name), author_id in self._id_lookup.items():
                author = AuthorID(project_name, author_name, author_id)
                json.dump(author.__dict__, f, ensure_ascii=False)
                f.write('\n')

        

    def __generate_author_id(self, project_name, author_name):
        name_pair = (project_name, author_name)
        if name_pair not in self._id_lookup:
            author_id_str = f"A{self.id_start}"
            self._id_lookup[name_pair] = author_id_str
            self._name_lookup[author_id_str] = name_pair
            self.id_start += 1
        self.save(self.output_path)
        

    def get_author_id(self, project_name: str, author_name: str) -> str:
        id = self._id_lookup.get((project_name, author_name))
        if id is None:
            self.__generate_author_id(project_name, author_name)
            return self._id_lookup[(project_name, author_name)]
        return id

    def get_author_name(self, author_id: str):
        return self._name_lookup[author_id][1]
    
    def get_author_project(self, author_id: str):
        return self._name_lookup[author_id][0]
    
    def get_author_ids(self, project_name):
        ids = []
        for key, value in self._id_lookup.items():
            if project_name in key:
                ids.append(value)
        return ids
        

@dataclass
class MethodTagResult:
    package: str
    filepath: str
    class_path: str
    method_name: str
    signature: str
    line_range: List[int]
    author_lines_map: Dict[str, List[Tuple[int, int]]]
    author_edit_ratio_map: Dict[str, float]
    dominant_author: str
    test_class:str # qualified test class
    code:str=""

    
@dataclass
class FileTagResult:
    package: str
    filepath: str
    lines: int
    author_lines_map: Dict[str, List[Tuple[int, int]]]
    dominant_author: str
    author_edit_ratio_map: Dict[str, float]    
    test_class: str
    
    
def load_file_tag_results(csv_path: str) -> List[FileTagResult]:
    df = pd.read_csv(csv_path)
    results = []
    for _, row in df.iterrows():
        result = FileTagResult(
            package=row['package'],
            filepath=row['filepath'],
            lines=ast.literal_eval(row['lines']) if isinstance(row['lines'], str) else row['lines'],
            author_lines_map=ast.literal_eval(row['author_lines_map']),
            dominant_author=row['dominant_author'],
            author_edit_ratio_map=ast.literal_eval(row['author_edit_ratio_map']),
            # authorship=row['authorship'],
            test_class=row["test_class"]
        )
        results.append(result)
    return results

def load_method_tag_results(csv_path: str) -> List[MethodTagResult]:
    df = pd.read_csv(csv_path)
    results = []
    for _, row in df.iterrows():
        result = MethodTagResult(
            package=row['package'],
            filepath=row['filepath'],
            class_path=row['class_path'],
            method_name=row['method_name'],
            signature=row['signature'],
            line_range=ast.literal_eval(row['line_range']),
            author_lines_map=ast.literal_eval(row['author_lines_map']),
            author_edit_ratio_map=ast.literal_eval(row['author_edit_ratio_map']),
            dominant_author=row['dominant_author'],
            test_class=row["test_class"],
            code=row["code"] if "code" in row else "",
        )
        results.append(result)
    return results

    
class TestClassExtractor:
    def __init__(self, src_file, package) -> None:
        self.tested_methods = None
        self.src_file = src_file
        self.package = package
    
    def get_test_file(self, dir, test_filenames):
        if not os.path.exists(dir):
            return ""
        
        test_filename = ""
        for file in os.listdir(dir):
            if file.lower() in test_filenames:
                test_filename = file
                break
            if os.path.isdir(os.path.join(dir, file)):
                test_filename = self.get_test_file(os.path.join(dir, file), test_filenames)
                if test_filename:
                    break
        if test_filename:
            return os.path.join(dir, test_filename)
        return ""
            
    def get_tested_methods(self, src_file, package) -> list[Method]:
        # 根据src_file获取test file
        dir, filename = os.path.split(src_file)
        test_dir, test_filename = "", ""
        if "src/main/java" in dir:
            test_dir = dir.replace("src/main/java", "src/test/java")
        else:
            return []
        
        # 可能的文件名
        base_name, ext = os.path.splitext(filename)
        test_filenames = [
            filename.lower(),
            f"{base_name}Test{ext}".lower(),
            f"Test{base_name}{ext}".lower(),
            f"{base_name}Tests{ext}".lower(),
            f"{base_name}_Test{ext}".lower()
        ]
        
        test_filepath = self.get_test_file(test_dir, test_filenames)
        
        global test_classes
        if test_filepath:
            test_classes.add(test_filepath)

        if os.path.exists(test_filepath):
            parser = JavaTreeSitterParser(test_filepath)
            return parser.get_methods()
        return []
    
    def get_test_class(self, method_name):
        if self.tested_methods is None:
            self.tested_methods = self.get_tested_methods(self.src_file, self.package)
            
        testname_candidates = [
            method_name,
            f"test{method_name}".lower(),
            f"{method_name}test".lower(),
            f"{method_name}tests".lower(),
            f"{method_name}_test".lower(),
            f"{method_name}_tests".lower()
        ]
        for test_method in self.tested_methods:
            for candidate_name in testname_candidates:
                if candidate_name in test_method.method_name.lower():
                    return test_method.class_path
        return ""
    
    
class FileLevalTestClassExtractor(TestClassExtractor):
    def get_test_class(self, method_name):
        dir, filename = os.path.split(self.src_file)
        test_dir, test_filename = "", ""
        if "src/main" in dir:
            test_dir = dir.replace("src/main", "src/test")
        else:
            return ""
        
        # 可能的文件名
        base_name, ext = os.path.splitext(filename)
        test_filenames = [
            filename.lower(),
            f"{base_name}Test{ext}".lower(),
            f"Test{base_name}{ext}".lower(),
            f"{base_name}Tests{ext}".lower(),
            f"{base_name}_Test{ext}".lower()
        ]
        test_filepath = self.get_test_file(test_dir, test_filenames)
        return test_filepath
        

class AuthorTagger:
    AUTHOR_ID_MAP = AuthorIDMap()
    AUTHOR_ID_MAP.load()

    def __init__(self, project_config:ProjectConfig):
        self.project_config = project_config
       

    def do_tag_file(self, file_path: str) -> FileTagResult:
        rel_path = os.path.relpath(file_path, self.project_config.repo_path)

        # Step 1: 获取 git blame 信息
        tmp_output = f"tmp{random.randint(1, 100)}.txt"
        with open(tmp_output, "w", encoding="utf-8") as f:
            subprocess.run(
                ["git", "blame", "--line-porcelain", rel_path],
                cwd=self.project_config.repo_path,
                encoding="utf-8",
                stdout=f
            )

        blame_results = []
        raw_author_lines = defaultdict(list)
        current_author = None

        with open(tmp_output, "r", encoding="utf-8") as f:
            blame_results = f.readlines()

        idx = 1
        while idx < len(blame_results):
            line_range_strs = blame_results[idx - 1].split()
            line_number = int(line_range_strs[2])

            # print("".join(blame_results[idx - 12:]))
            
            author_strs = blame_results[idx].split()
            current_author = " ".join(author_strs[1:]).replace(" ", "-")
            current_author = AuthorTagger.AUTHOR_ID_MAP.get_author_id(self.project_config.name, current_author)
            if current_author is None:
                print(f"authro is None")
                exit(0)
            
            raw_author_lines[current_author].append(line_number)
            
            idx += 12
            while idx < len(blame_results) and blame_results[idx].split()[0] != "author":
                idx += 1
       
       # 计算编辑比例和主导作者
        with open(file_path, 'r') as f:
            total_lines = len(f.readlines())
            author_edit_ratio_map = {
                author: len(line_indices) / total_lines
                for author, line_indices in raw_author_lines.items()
            }
            max_ratio = max(author_edit_ratio_map.items(), key=lambda x: x[1])[1]
            dominant_author = max(author_edit_ratio_map.items(), key=lambda x: x[1])[0] if author_edit_ratio_map else ""

        # 转换为连续区间形式
        author_lines_map = {
            author: self._group_consecutive_lines(line_indices)
            for author, line_indices in raw_author_lines.items()
        }

        # 获取包名
        package = self._extract_package(blame_results)
        
        os.remove(tmp_output)
        test_extractor = FileLevalTestClassExtractor(file_path, package)

        return FileTagResult(
            package=package,
            filepath=file_path,
            lines=total_lines,
            author_lines_map=author_lines_map,
            author_edit_ratio_map=author_edit_ratio_map,
            dominant_author=dominant_author,
            test_class=test_extractor.get_test_class("")
        )
        
    def tag_methods(self, file_results: List[FileTagResult]) -> List[MethodTagResult]:
        method_tag_results = []

        for file_result in file_results:

            parser = JavaTreeSitterParser(file_result.filepath)
            methods = parser.get_methods()
            
            test_extractor = TestClassExtractor(file_result.filepath, file_result.package)
            for method in methods:
                start_line, end_line = method.line_range
                method_lines = list(range(start_line, end_line + 1))

                author_lines_map = {}
                for author, ranges in file_result.author_lines_map.items():
                    matched = []
                    for r_start, r_end in ranges:
                        for line in method_lines:
                            if r_start <= line <= r_end:
                                matched.append(line)
                    if matched:
                        author_lines_map[author] = matched

                total = len(method_lines)
                author_edit_ratio_map = {
                    author: len(line_indices) / total
                    for author, line_indices in author_lines_map.items()
                }
                dominant_author = max(author_edit_ratio_map.items(), key=lambda x: x[1])[0] if author_edit_ratio_map else ""

                # 转换为连续区间形式
                author_lines_map = {
                    author: self._group_consecutive_lines(line_indices)
                    for author, line_indices in author_lines_map.items()
                }
                
                test_class = test_extractor.get_test_class(method.method_name)
                qualified_test_class = f"{file_result.package}.{test_class}" if test_class else ""
                with open(file_result.filepath, 'r', encoding='utf-8') as f:
                    lines = f.readlines()
                code = "".join(lines[start_line - 1:end_line])

                method_tag_results.append(MethodTagResult(
                    package=file_result.package,
                    filepath=file_result.filepath,
                    class_path=method.class_path,
                    method_name=method.method_name,
                    signature=method.signature,
                    line_range=[start_line, end_line],
                    author_lines_map=author_lines_map,
                    author_edit_ratio_map=author_edit_ratio_map,
                    dominant_author=dominant_author,
                    test_class=file_result.test_class,
                    code=code
                ))

        return method_tag_results



    def tag_file(self, filepath, file_suffix):
        if filepath.endswith(file_suffix):
            result = self.do_tag_file(filepath)
            return [result]
                    
        dir = filepath
        result = []
        if os.path.isdir(filepath):
            for file in os.listdir(dir):
                result += self.tag_file(os.path.join(dir, file), file_suffix)

        return result


    def tag_project(self):
        repo_path = self.project_config.repo_path
        file_results, method_results = [], []
        for dir in self.project_config.include_dirs:
            file_results += self.tag_file(os.path.join(repo_path, dir), self.project_config.allowed_suffix)
        method_results = self.tag_methods(file_results)
        return file_results, method_results
    
    def _group_consecutive_lines(self, indices: List[int]) -> List[Tuple[int, int]]:
        if not indices:
            return []
        indices = sorted(indices)
        chunks = []
        start = prev = indices[0]
        for idx in indices[1:]:
            if idx == prev + 1:
                prev = idx
            else:
                chunks.append((start, prev))
                start = prev = idx
        chunks.append((start, prev))
        return chunks

    def _extract_package(self, lines: List[str]) -> str:
        for line in lines:
            match = re.match(r'\s*package\s+([\w\.]+);', line)
            if match:
                return match.group(1)
        return ""


def write_to_csv(result_list, csv_path: str):
    titles = list(asdict(result_list[0]).keys())
    if not os.path.exists(os.path.dirname(csv_path)):
        os.makedirs(os.path.dirname(csv_path), exist_ok=True)
    with open(csv_path, 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(titles)

        for result in result_list:
            result_dict = asdict(result)
            writer.writerow([result_dict[title] for title in titles])

def count_file(dir=r"/data/jyy/style/code-style-transformation/projects-data/projects/Stirling-PDF/src/test"):
    classes = set()
    for file in os.listdir(dir):
        if file.endswith(".java"):
            classes.add(os.path.join(dir, file))
        else:
            classes.union(count_file(os.path.join(dir, file)))
    return classes


def check_test_classes(dir):
    global test_classes
    
    full_classes = count_file(dir)
    diff = set(full_classes) - set(test_classes)
    if diff:
        print("Test classes have not been extracted:")
        print(diff)
    
    test_classes = set()
    


if __name__ == "__main__":
    projectConfigs = ProjectConfigs()
    projectConfigs.load_configs('projects-config.json')
    for project_config in projectConfigs.projects:
        if os.path.exists(project_config.method_tag_output_csv):
            continue
        print(f"Tag project:{project_config.name}")
        tagger = AuthorTagger(project_config)
        file_results, method_results = tagger.tag_project()
        write_to_csv(file_results, project_config.file_tag_output_csv)
        write_to_csv(method_results, project_config.method_tag_output_csv)
        
        # for dir in project_config.include_dirs:
        #     check_test_classes(os.path.join(project_config.repo_path, dir).replace("src/main", "src/test"))
    

