import os
import json
import re
import shutil
import subprocess
import numpy as np
import json
import pandas as pd
import csv
import fire
import random

import matplotlib.pyplot as plt
import sys
sys.path.append('../')
sys.path.append('Forsee')

from dataclasses import dataclass, asdict
from collections import defaultdict

from utils import *
from config import ProjectConfigs
from path import *

FORSEE_WORKDIR = "./lib/Forsee"

def create_forsee_data( output_dir, data_dict, merge_method=False, override=True):
    if not override and os.path.exists(output_dir):
        return
    
    if os.path.exists(output_dir):
        shutil.rmtree(output_dir)
    for author in data_dict:
        target_dir = os.path.join(output_dir, author)
        os.makedirs(target_dir, exist_ok=True)
        
        if merge_method:
            # 合并模式：将同一文件内的方法聚集在一起
            file_map = defaultdict(list)  # file_path -> list of ResultWrapper
            for d in data_dict[author]:
                file_map[d.get_filepath()].append(d)

            for file_path, methods in file_map.items():
                with open(file_path, 'r') as f:
                    code_lines = f.readlines()

                merged_code = []
                for d in methods:
                    start, end = d.get_line_range()
                    snippet = code_lines[start - 1:end]
                    merged_code.append("".join(snippet) + "\n")

                filename = os.path.splitext(os.path.basename(file_path))[0]
                with open(os.path.join(target_dir, f"{d.id}.java"), 'w') as f:
                    f.writelines(merged_code)
        else:
            # 单独模式：每个方法单独写一个文件
            for d in data_dict[author]:
                with open(os.path.join(target_dir, f"{d.id}.java"), 'w') as f:
                    f.write(d.get_code())
                    
def balance_data(data, max_multiple=3, seed=42):
    def get_author_line_counts(data_dict):
        counts = defaultdict(int)
        for author, data_list in data_dict.items():
            for d in data_list:
                counts[author] += d.get_line_count()
        return counts
    
    # 1. 统计每个作者的代码行数
    author_counts = get_author_line_counts(data)
    # print(author_counts)
    
    if not author_counts:
        return

    # 2. 获取最小数量
    min_count = min([v for v in author_counts.values() if v > 0])
    max_allowed = min_count * max_multiple

    # 3. 平衡数据
    random.seed(seed)
    balanced_data = {}
    for author, result_list in data.items():
        if author_counts[author] <= max_allowed:
            balanced_data[author] = result_list
        else:
            # 随机采样，确保不超过 max_allowed
            tmp_list = [d for d in result_list]
            random.shuffle(tmp_list)
            new_result = []
            total_lines = 0
            for d in tmp_list:
                new_result.append(d)
                total_lines += d.get_line_count()
                if total_lines >= max_allowed:
                    break
            balanced_data[author] = new_result

    return balanced_data

def get_forsee_dataset_dir_id(root_dir, dir_prefix, authors):
    candidates = [dir for dir in os.listdir(root_dir) if re.match(rf'^{re.escape(dir_prefix)}\d+$', dir) ]
    max_id = 0
    for dirname in candidates:
        match = re.search(r'(\d+)$', dirname)
        id = int(match.group(1))
        existed_authors = [dir for dir in os.listdir(os.path.join(root_dir, dirname))]
        if sorted(existed_authors) == sorted(authors):
            return id
        if id > max_id:
            max_id = id
    return max_id + 1
            
        
                    
# Create data for binary classfication task               
def create_binary_forsee_data(data_dict, output_dir, override=True):
    def get_name_in_project(author_name):
        for project_name in ProjectConfigs().get_all_project_names():
            prefix = f"{project_name}-"
            if author_name.startswith(prefix):
                return author_name.replace(prefix, "")
        return author_name
    
                
    target_dir, basename = os.path.split(output_dir)
    authors = list(data_dict.keys())    

    group_line_counts = []  # 记录每个author group的总代码行数

    author_pairs = []
    valid_dirs = []
    # 遍历每一对作者进行组合
    for i in range(len(authors)):
        for j in range(i + 1, len(authors)):
            author1, author2 = authors[i], authors[j]
            data1, data2 = data_dict[authors[i]], data_dict[authors[j]]
            # name1_in_project, name2_in_project = get_name_in_project(author1), get_name_in_project(author2)

            # 创建数据集
            training_data = defaultdict(list)
            training_data[author1] = data1.training_data[author1]
            training_data[author2] = data2.training_data[author2]

            author1_lines_train = sum(d.get_line_count() for d in training_data[author1])
            author2_lines_train = sum(d.get_line_count() for d in training_data[author2])

            training_data = balance_data(training_data)


            # 统计这一组作者的总代码行数
            total_lines = sum(d.get_line_count() for d in training_data[author1] + training_data[author2])
            group_line_counts.append(total_lines)
            
            eval_data = defaultdict(list)
            eval_data[author1] = data1.transformation_data[author1]
            eval_data[author2] = data2.transformation_data[author2]

            # 确保即使添加了author pair，之前已经存在的author pair能够重新映射到相同的目录
            dir_id = get_forsee_dataset_dir_id(target_dir, basename, [author1, author2])
            training_dir = os.path.join(target_dir, f"{basename}{dir_id}")
            # 保存forsee数据集
            eval_dir = os.path.join(target_dir, f"{basename}{dir_id}-eval")
            create_forsee_data(training_dir, training_data)
            create_forsee_data(eval_dir, eval_data)

            author_pairs.append((author1, author2))
            
    return author_pairs

@dataclass
class ClassificationResult:
    fid:str
    author_name:str # authorship
    author_id:int
    author_ids:list[int]
    confidence_list:list[float]
    top1id:str
    top5id:str
    project_name:str=""
    origin_author:str=""
    
    def istop1_success(self):
        return self.author_id == self.top1id
    
    def from_dict(row):
        return ClassificationResult(
                        fid=row['fid'],
                        author_name=row['author_name'],
                        author_id=int(row['author_id']),
                        author_ids=row['author_ids'],
                        confidence_list=row['confidence_list'],
                        top1id=row['top1id'],
                        top5id=row['top5id'],
                        project_name=row['project_name'] if 'project_name' in row else "",
                        origin_author=row['origin_author'] if 'origin_author' in row else ""
                    )


class ClassificationResultCollection:
    def __init__(self, result_path=""):
        self.result_path = result_path
        self.data = {}
        self.output_path = None
        
        self.max_cache = 10
        self.cache_count = self.max_cache
        
        self.load_result()

    def load_result(self):
        if not os.path.exists(self.result_path):
            return
        self.data = {}
        with open(self.result_path, 'r', encoding='utf-8') as f:
            for line in f:
                try:
                    row = json.loads(line.strip())
                    result =ClassificationResult.from_dict(row)
                    key = self._create_key(result)
                    if key not in self.data:
                        self.data[key] = []
                    self.data[key].append(result)
                except Exception as e:
                    print(f"[Error] Failed to parse line: {line}\nReason: {e}")

    def _create_key(self, result:ClassificationResult):
        return (result.origin_author, result.fid)
    
    def update_origin_author_for_all(self, old, new):
        if old == new:
            return
        new_data = {}
        for key, value in self.data.items():
            if key[0] == old:
                for v in value:
                    v.origin_author = new
                new_data[(new, key[1])] = value
            else:
                new_data[key] = value
        self.data = new_data
                    
    def get_result(self,author_name, fid, project_name, origin_author="") -> ClassificationResult:
        origin_author = origin_author if origin_author else author_name
        key = (origin_author, fid)
        if key in self.data:
            author_results : list[ClassificationResult] = self.data[key]
            for result in author_results:
                if result.author_name == author_name and result.project_name == project_name:
                    return result
        return None
    
    def get_result_list(self) -> list[ClassificationResult]:
        return [item for sublist in self.data.values() for item in sublist]
    
    def get_authors(self):
        authors = [key[0] for key in self.data.keys() if key[0]]
        result = list(set(authors))
        if result:
            return result

        result_list =self.get_result_list()
        authors = set()
        for r in result_list:
            authors.add(r.author_name)
        return list(authors)
    
    def add_results(self, other):
        result_list = other.get_result_list()
        for r in result_list:
            key = self._create_key(r)
            if key not in self.data:
                self.data[key] = []
            self.data[key].append(r)
        self.cache_count -= len(result_list)
        
        if self.cache_count <= 0:
            self.save_to()
        self.cache_count = self.max_cache

    def add_results_from_list(self, result_list):
        for r in result_list:
            key = self._create_key(r)
            if key not in self.data:
                self.data[key] = []
            self.data[key].append(r)

    def save_to(self, output_path=None):
        if self.output_path is not None:
            output_path = self.output_path
            
        if os.path.dirname(output_path):
            os.makedirs(os.path.dirname(output_path), exist_ok=True)

        data_list = [d for l in self.data.values() for d in l]
        with open(output_path, 'w', encoding='utf-8') as f:
            for result in data_list:
                json_line = json.dumps(asdict(result), ensure_ascii=False)
                f.write(json_line + '\n')
    
    
def show_top1_acc(project_name):
    path = create_forsee_eval_path(project_name)
    if not os.path.exists(path):
        return
    
    result = ClassificationResultCollection(path)
    result.load_result()

    correct_counts = {}
    total_counts = {}

    for author_name, result_list in result.data.items():
        total = len(result_list)
        correct = sum(r.istop1_success() for r in result_list)
        total_counts[author_name] = total
        correct_counts[author_name] = correct

    author_names = sorted(total_counts.keys())
    accuracies = [correct_counts[name] / total_counts[name] for name in author_names]

    # 绘制柱状图
    plt.figure(figsize=(10, 6))
    plt.bar(author_names, accuracies, color='skyblue')
    plt.ylabel('Top-1 Accuracy')
    plt.title(f'Top-1 Accuracy by Author in Project: {project_name}')
    plt.xticks(rotation=45, ha='right')
    plt.ylim(0, 1)
    plt.tight_layout()
    output_path = os.path.join(os.path.dirname(path), "png", f"{project_name}.png")
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    plt.savefig(output_path)


class BatchBinClassificationResult:
    def __init__(self, project_name, acc_pair_csv_path=""):
        self.project_name = project_name
        self.acc_pair_csv_path = acc_pair_csv_path
        self.pair_acc_map = {}      # (author1, author2) -> (acc1, acc2)
        self.author_acc_map = defaultdict(list)  # author -> [acc1, acc2, ...]
        self.load(self.acc_pair_csv_path)
            
    def save_binary_acc(self, project_name): 
        path = create_forsee_eval_path(project_name)
        result_dir = os.path.dirname(path)
        output_dir = os.path.join(result_dir, "csv")
        os.makedirs(output_dir, exist_ok=True)

        acc_pair_dict = {}  # key: (author1, author2), value: (acc1, acc2)
        author_accs = defaultdict(list)  # key: author, value: list of their acc in each binary task

        for file in os.listdir(result_dir):
            if file.startswith(project_name) and file.endswith(".jsonl"):

                binary_path = os.path.join(result_dir, file)
                result = ClassificationResultCollection(binary_path)
                result.load_result()
                
                if len(result.get_authors()) != 2:
                    continue

                author1, author2 = result.get_authors()

                # 统计每个作者的准确率
                acc_map = {}
                for key, result_list in result.data.items():
                    total = len(result_list)
                    correct = sum(r.istop1_success() for r in result_list)
                    acc = correct / total if total > 0 else 0.0

                    if key[0]:
                        acc_map[key[0]] = acc
                    else:
                        acc_map[result_list[0].author_name] = acc

                acc1 = acc_map.get(author1, 0.0)
                acc2 = acc_map.get(author2, 0.0)

                # 存入 author pair 结构（排序保证唯一性）
                pair_key = tuple(sorted([author1, author2]))
                acc_pair_dict[pair_key] = (acc1, acc2)

                # 更新每个作者的准确率列表
                author_accs[author1].append(acc1)
                author_accs[author2].append(acc2)

        # 保存 author pair 的准确率信息为 CSV
        pair_output_path = create_forsee_bin_classification_acc_path(project_name)
        with open(pair_output_path, mode='w', encoding='utf-8', newline='') as f:
            writer = csv.writer(f)
            writer.writerow(['author1', 'author2', 'acc_author1', 'acc_author2'])
            for (a1, a2), (acc1, acc2) in acc_pair_dict.items():
                writer.writerow([a1, a2, acc1, acc2])
        print(f"Saved author pair accuracy results to: {pair_output_path}")
        self.acc_pair_csv_path = pair_output_path

        # 计算统计指标
        stats = {
            'author': [],
            'min_acc': [],
            'max_acc': [],
            'mean_acc': [],
            'median_acc': []
        }

        for author, accs in author_accs.items():
            accs = pd.Series(accs)
            stats['author'].append(author)
            stats['min_acc'].append(float(accs.min()))
            stats['max_acc'].append(float(accs.max()))
            stats['mean_acc'].append(float(accs.mean()))
            stats['median_acc'].append(float(accs.median()))

        stats_df = pd.DataFrame(stats)
        stats_output_path = os.path.join(output_dir, f"{project_name}-acc-stats.csv")
        stats_df.to_csv(stats_output_path, index=False)
        print(f"Saved accuracy statistics to: {stats_output_path}")
        
        
    def load(self, path):
        if not os.path.exists(path):
            self.save_binary_acc(self.project_name)
            path = self.acc_pair_csv_path
            
        df = pd.read_csv(path)
        for _, row in df.iterrows():
            a1, a2 = row['author1'], row['author2']
            acc1, acc2 = float(row['acc_author1']), float(row['acc_author2'])
            self.pair_acc_map[(a1, a2)] = (acc1, acc2)
            self.author_acc_map[a1].append(acc1)
            self.author_acc_map[a2].append(acc2)

    def get_pair_result(self, author1, author2):
        return self.pair_acc_map.get((author1, author2))

    def get_author_acc_list(self, author):
        return self.author_acc_map.get(author, [])

    def get_author_stats(self, author):
        accs = self.get_author_acc_list(author)
        if not accs:
            return {'mean': 0.0, 'min': 0.0, 'max': 0.0, 'median': 0.0}
        accs_np = np.array(accs)
        return {
            'mean': float(accs_np.mean()),
            'min': float(accs_np.min()),
            'max': float(accs_np.max()),
            'median': float(np.median(accs_np))
        }

    def save_pair_result(self, path):
        rows = []
        for (a1, a2), (acc1, acc2) in self.pair_acc_map.items():
            rows.append({
                'author1': a1,
                'author2': a2,
                'acc_author1': acc1,
                'acc_author2': acc2
            })
        df = pd.DataFrame(rows)
        df.to_csv(path, sep='\t', index=False)

    def save_author_stats(self, path):
        rows = []
        for author in sorted(self.author_acc_map.keys()):
            stats = self.get_author_stats(author)
            rows.append({
                'author': author,
                'mean_acc': stats['mean'],
                'min_acc': stats['min'],
                'max_acc': stats['max'],
                'median_acc': stats['median']
            })
        df = pd.DataFrame(rows)
        df.to_csv(path, index=False)
        
        
    def get_pairs_by_acc(self, min_acc):
        result = []
        for author_pairs, acc_list in self.pair_acc_map.items():
            if acc_list[0] >= min_acc and acc_list[1] >= min_acc:
                result.append(author_pairs)
        return result
    
    def print_pairs_by_acc(self, min_acc):
        print(f"Classifiers(accuracy >= {min_acc})=======================================================================")
        result = []
        for author_pairs, acc_list in self.pair_acc_map.items():
            if acc_list[0] >= min_acc and acc_list[1] >= min_acc:
                print(f"{author_pairs},{acc_list}")
        print("==========================================================================================================")
                
    def get_all_pairs(self):
        return self.pair_acc_map.keys()


class ClassifierManager:
    def __init__(self, dir="./lib/Forsee/dataset"):
        self.classifier_map = {} # key:(project_name, author1, author2) value:dirname
        self.__init_map(dir)
    
    def __init_map(self, classifiers_dir):
        for dirname in os.listdir(classifiers_dir):
            full_path = os.path.join(classifiers_dir, dirname)
            if not os.path.isdir(full_path):
                continue

            # 去除目录末尾的数字，例如 "jedis123" -> "jedis"
            project_name = re.sub(r"\d+$", "", dirname)

            # 获取子目录（作者目录），并排序
            try:
                author_dirs = sorted(os.listdir(full_path))
                if len(author_dirs) < 2:
                    continue
                author1, author2 = author_dirs[:2]
                key = (project_name, author1, author2)
                self.classifier_map[key] = dirname
            except Exception as e:
                print(f"Warning: failed to process {dirname} due to {e}")

    
    def get_classifier_dirname(self, project_name, authors:list[str]):
        author1, author2 = sorted(authors)
        return self.classifier_map[(project_name, author1, author2)]
    
    def get_cache_dir(self, project_name, authors:list[str]):
        dir = self.get_classifier_dirname(project_name, authors)
        return dir.replace("Forsee/dataset", "Forsee/caches")


# `project_name` is the project name of the `authorship`
# or `project_name` may be empty in across project case.
def run_binary_classifier(project_name, src_author, target_author, filename_code_dict, authorship) -> ClassificationResultCollection:
    classifier_manager = ClassifierManager()
    
    # create forsee dataset
    dir = create_forsee_eval_codes_dir(project_name)
    if os.path.exists(dir):
        shutil.rmtree(dir)
    os.makedirs(os.path.join(dir, src_author))
    os.makedirs(os.path.join(dir, target_author))

    codes_dir = os.path.join(dir, authorship)
    for filename, code in filename_code_dict.items():
        with open(os.path.join(codes_dir, filename), "w", encoding="utf-8") as f:
            f.write(code)
        
        
    classifier_dirname = classifier_manager.get_classifier_dirname(project_name, [src_author, target_author])
    eval_dirname = os.path.basename(dir)
    
    cmd = [
        "python", "classify.py", "classify_dataset",
        f"caches/{classifier_dirname}", 
        f"dataset/{eval_dirname}", f"caches/{eval_dirname}",
        os.path.join(FORSEE_EVAL_RESULT_DIR, f"{eval_dirname}-result.jsonl"),
        "--device=gpu"
    ]
    print(" ".join(cmd))

    # 移除forsee运行缓存：
    shutil.rmtree(os.path.join(FORSEE_CACHES, eval_dirname), ignore_errors=True)
    subprocess.run(cmd, cwd=FORSEE_WORKDIR, check=True)
    eval_result_path = create_forsee_eval_path(project_name)
    result = ClassificationResultCollection(eval_result_path)
    
    for r in result.get_result_list():
        r.project_name = project_name
    return result


def classify_transformed_results(transformation_method, min_target_lines) -> ClassificationResultCollection:
    from dataset import TransformationData, TransformPair
    from transform import ResultManager
    
    transformed_classify_path = create_transformed_classify_results_path(transformation_method, min_target_lines)
    if os.path.exists(transformed_classify_path):
        result_collection = ClassificationResultCollection(transformed_classify_path)
    else:
        result_collection = ClassificationResultCollection()
    result_collection.output_path = transformed_classify_path
    
    result_path = create_transformation_result_jsonl_path(transformation_method, min_target_lines)
    result_manager = ResultManager(result_path)
    pair_caches = {}

    # 按照project进行分组运行forsee，避免分类结果被覆盖
    for results in result_manager.get_project_classified_results().values():

        # 合并同一个author pair的数据
        result_pack = {}
        for r in results:
            key = (r.project_name, r.pair_id)
            if key not in pair_caches:
                data = TransformationData.load_data(create_transformation_pair_path(r.project_name, min_target_lines))
                for d in data:
                    pair_caches[(r.project_name, d.pair_id)] = d
            if key in pair_caches:
                src_author, target_author = pair_caches[key].src_author, pair_caches[key].target_author

                if (r.project_name, src_author, target_author) not in result_pack:
                    result_pack[(r.project_name, src_author, target_author)] = []
                result_pack[(r.project_name, src_author, target_author)].append(r)

        # 运行forsee分类器
        invalid_results = []
        for (project_name, src_author, target_author), results in result_pack.items():
            unclassified_results = [r for r in results if result_collection.get_result(target_author, r.src_id, project_name, src_author) is None]
            # check exists
            codes_map = {f"{r.src_id}.java":r.code for r in unclassified_results}
            if not codes_map:
                continue
            
            tmp_result_collection = run_binary_classifier(project_name, src_author, target_author, codes_map, target_author)
            tmp_result_collection.update_origin_author_for_all("", src_author)
            result_collection.add_results(tmp_result_collection)

            
            manual_results = []
            for r in unclassified_results:
                if tmp_result_collection.get_result(target_author, r.src_id, project_name, src_author) is not None:
                    r.successful_trans = tmp_result_collection.get_result(target_author, f"{r.src_id}", project_name, src_author).istop1_success()
                else:
                    # code are too short
                    invalid_results.append((project_name, r.pair_id))
                    # create failure classification result manually
                    manual_results.append(ClassificationResult(r.src_id, target_author, 1, [0, 1], [1, -1], 0, [0, 1], r.project_name, src_author))

            result_collection.add_results_from_list(manual_results)
                    
        # result_manager.remove(invalid_results)
        result_manager.update_all()
            
            
    result_collection.save_to()
    
    # print successful rate
    # count = 0
    # success_count = len([r.successful_trans for r in result_manager.get_all_results() if r.successful_trans])
    # print(round(success_count / result_manager.get_size() * 100, 2))


    return result_collection


def remove_low_accuracy_classifiers(accuracy_threshold):
    classifier_manger = ClassifierManager()
    for project_name in ProjectConfigs().get_all_project_names():
        classify_result = BatchBinClassificationResult(project_name, create_forsee_bin_classification_acc_path(project_name))
        satisfied_pairs = classify_result.get_pairs_by_acc(accuracy_threshold)
        classify_result.print_pairs_by_acc(accuracy_threshold)
        for authors in classify_result.get_all_pairs():
            if authors not in satisfied_pairs:
                dirname = classifier_manger.get_classifier_dirname(project_name, list(authors))
                shutil.rmtree(os.path.join(FORSEE_DATASET_DIR, dirname), ignore_errors=True)
                shutil.rmtree(os.path.join(FORSEE_CACHES, dirname), ignore_errors=True)

    
if __name__ == '__main__':
    # python forsee_eval.py deepseek-r1-0528--free 200
    # python forsee_eval.py egsi 200
    
    fire.Fire()
    
    
    
    # 保存并打印二分类结果
    # projects_config = ProjectConfigs()
    # for project in projects_config.projects:
    #     # show_top1_acc(project.name)
    #     result = BatchBinClassificationResult(project.name)
    #     result.print_pairs_by_acc(0.8)
        
        
