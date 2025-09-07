import json
import os
import shutil
import subprocess
import numpy as np
import math
from itertools import combinations
from typing import List, Dict, Tuple
import random
import csv
import sys
import statistics
import fire
import re

from scipy.special import softmax


import matplotlib.pyplot as plt
from collections import defaultdict
from dataclasses import asdict,dataclass
from utils import *
from author_tagger  import AuthorIDMap
from forsee import ClassifierManager

AUTHOR_ID_MAP = AuthorIDMap()
AUTHOR_ID_MAP.load()

#from codebleu import calc_codebleu

from config import ProjectConfig, ProjectConfigs
from author_tagger import (
    FileTagResult, MethodTagResult, 
    load_file_tag_results, load_method_tag_results,
    write_to_csv
)
from utils import *
from myparser import JavaTreeSitterParser
from forsee import (
    BatchBinClassificationResult, ClassificationResultCollection,
    create_binary_forsee_data
)


class MethodWrapper:
    def __init__(self, result) -> None:
        self.instance = result
        self.id = ""
        
        
    def get_dominant_author(self):
        return self.instance.dominant_author
    
    def get_author_edit_ratio_map(self):
        return self.instance.author_edit_ratio_map
    
    
    def get_test_class(self):
        if type(self.instance.test_class) == str:
            class_name = os.path.basename(self.instance.test_class).replace(".java", "")
            return f"{self.get_package()}.{class_name}"
        return self.instance.test_class
    
    def get_author_lines_map(self):
        return self.instance.author_lines_map
    
    def get_filepath(self):
        return self.instance.filepath
    
    def get_line_range(self) -> List[int]:
        if isinstance(self.instance, MethodTagResult):
            return self.instance.line_range
        return[]
    
    def get_line_count(self):
        if isinstance(self.instance, MethodTagResult):
            return self.instance.line_range[1] - self.instance.line_range[0] + 1
        elif isinstance(self.instance, FileTagResult):
            return self.instance.lines
        return 0
    
    def get_package(self):
        return self.instance.package
    
    def get_method_name(self):
        if isinstance(self.instance, MethodTagResult):
            return self.instance.method_name
        return ""
    
    def set_line_range(self, new_range: List[int]):
        if isinstance(self.instance, MethodTagResult):
            self.instance.line_range = new_range
            
    def get_code(self) -> str:
        return self.instance.code
    
            
    def get_only_code_lines_count(self):
        code = self.instance.code
        # 去除多行注释（/* ... */ 和 /** ... */）
        code = re.sub(r'/\*.*?\*/', '', code, flags=re.DOTALL)
        # 去除单行注释（// 开头的注释）
        code = re.sub(r'//.*', '', code)
        # 保留非空代码行
        lines = [line for line in code.splitlines() if line.strip()]
        return len(lines)

    
# def load_result_wrappers(src_path) -> List[ResultWrapper]:
#         results = []
#         with open(src_path, newline='', encoding='utf-8') as csvfile:
#             reader = csv.DictReader(csvfile)
#             for row in reader:
#                 try:
#                     method_result = MethodTagResult(
#                         package=row['package'],
#                         filepath=row['filepath'],
#                         class_path=row['class_path'],
#                         method_name=row['method_name'],
#                         signature=row['signature'],
#                         line_range=ast.literal_eval(row['line_range']),
#                         author_lines_map=ast.literal_eval(row['author_lines_map']),
#                         author_edit_ratio_map=ast.literal_eval(row['author_edit_ratio_map']),
#                         dominant_author=row['dominant_author'],
#                         test_class=row['test_class']
#                     )
#                     wrapper = ResultWrapper(method_result)
#                     wrapper.id = row['id']
#                     results.append(wrapper)
#                 except Exception as e:
#                     print(f"[Error] Failed to parse row: {row}\nReason: {e}")
#         return results

    
    
def save_statistic(data_dict, project_name, dataset_category):
        def get_author_stats(data_dict):
            author_file_count = defaultdict(int)
            author_line_total = defaultdict(int)

            for author, data_list in data_dict.items():
                author_file_count[author] += len(data_list)
                data_list:list[MethodWrapper]
                for d in data_list:
                    author_line_total[author] += d.get_line_count()

            # 计算平均行数
            author_avg_lines = {
                author: (author_line_total[author] / author_file_count[author]) if author_file_count[author] > 0 else 0
                for author in author_file_count
            }

            return author_file_count, author_avg_lines

        def plot_dual_distribution(author_counts, author_avg_lines, common_authors, title, output_path):
            authors = sorted(common_authors)
            file_counts = [author_counts[author] for author in authors]
            avg_lines = [author_avg_lines[author] for author in authors]

            x = np.arange(len(authors))  # X轴位置
            width = 0.35  # 柱子宽度

            fig, ax = plt.subplots(figsize=(12, 6))
            bars1 = ax.bar(x - width/2, file_counts, width, label='File Count', color='tab:blue')
            bars2 = ax.bar(x + width/2, avg_lines, width, label='Avg Lines/File', color='tab:orange')

            ax.set_xlabel("Author")
            ax.set_ylabel("Value")
            ax.set_title(title)
            ax.set_xticks(x)
            ax.set_xticklabels(authors, rotation=45, ha='right')
            ax.legend()

            # 标注柱子数值
            def autolabel(bars):
                for bar in bars:
                    height = bar.get_height()
                    ax.annotate(f'{height:.1f}' if isinstance(height, float) else f'{int(height)}',
                                xy=(bar.get_x() + bar.get_width() / 2, height),
                                xytext=(0, 3),  # 垂直方向偏移
                                textcoords="offset points",
                                ha='center', va='bottom')

            autolabel(bars1)
            autolabel(bars2)

            fig.tight_layout()
            plt.savefig(output_path)
            plt.close()
            
        def save_meta_to_csv(project_name, dataset_category, author_counts, author_avg_lines):
            meta_path = os.path.join(META_DATA_DIR, f'{dataset_category}-meta.csv')
            authors = list(author_counts.keys())
            total_authors = len(authors)
            total_files = sum(author_counts.values())
            avg_file_count = np.mean(list(author_counts.values())) if total_authors > 0 else 0
            avg_line_count = np.mean(list(author_avg_lines.values())) if total_authors > 0 else 0
            file_count_var = np.var(list(author_counts.values())) if total_authors > 0 else 0
            avg_lines_var = np.var(list(author_avg_lines.values())) if total_authors > 0 else 0

            header = ['Project Name', 'Total Authors', 'Total Files', 'Avg Files per Author', 'Avg Lines per File',
                    'File Count Variance', 'Avg Lines Variance']

            write_header = not os.path.exists(meta_path)

            with open(meta_path, 'a', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                if write_header:
                    writer.writerow(header)
                writer.writerow([
                    project_name, total_authors, total_files, avg_file_count, avg_line_count,
                    file_count_var, avg_lines_var
                ])

            
        output_dir = META_DATA_DIR
        os.makedirs(output_dir, exist_ok=True)

        file_counts, avg_lines_per_file = get_author_stats(data_dict)

        plot_dual_distribution(
            file_counts, avg_lines_per_file, data_dict.keys(),
            title="File Count and Avg Lines",
            output_path=os.path.join(output_dir, f"{project_name}-{dataset_category}.png")
        )
        
        save_meta_to_csv(project_name, dataset_category, file_counts, avg_lines_per_file)


class Data:
    def __init__(self, project:ProjectConfig, min_lines=5, min_dominant_author_edit=0.8):
        self.project = project
        self.training_data = {}
        self.transformation_data = {}
        
        data = self.load_data()
        data = self.filter_data(data, min_lines, min_dominant_author_edit)
        self.data_dict = self.to_data_dict(data)
        self.split_data(self.data_dict)

    def filter_data(self, data, min_lines, min_dominant_author_edit):
        # forsee good sample
        def sample_is_good(parser:JavaTreeSitterParser):
            if parser is None: return False
            # token number > 20
            if parser.get_tokens_count()<=20: return False
            # ast node > 10
            if parser.get_nodes_count()<=10: return False
            
            return True
        
        result = []
        id = 1
        for d in data:
            # 去除无法确定authorship的方法
            if  d.get_author_edit_ratio_map()[d.get_dominant_author()] <= min_dominant_author_edit:
                continue
            
            parser = JavaTreeSitterParser("")
            parser.parse_code_str(d.get_code())
            if not sample_is_good(parser):
                continue

            if d.get_line_count() < min_lines:
                continue
            
            d.id = f"M{id}"
            result.append(d)
            id += 1
        
        return result


    def to_data_dict(self, data_list):
        data_dict = {}
        for d in data_list:
            if d.get_dominant_author() not in data_dict:
                data_dict[d.get_dominant_author()] = []
            data_dict[d.get_dominant_author()].append(d)
        return data_dict


    def split_data(self, data_dict, train_percent=0.8, seed=42):
        """
        将数据按照每个作者划分为训练集和转换集。
        data: List[DataItem]，其中每个 item 都具有 get_dominant_author 方法。
        """
        random.seed(42)
        for author, data in data_dict.items():
            num_train = int(len(data)*train_percent)
            random.shuffle(data)
            training_data = data[:num_train]
            transformation_data = data[num_train:]
            self.training_data[author] = training_data
            self.transformation_data[author] = transformation_data
            
    def update_data(self, data_dict):
        self.data_dict = data_dict
        self.split_data(data_dict)

    def get_data_list(self, author) -> list[MethodWrapper]:
        return self.data_dict[author] if author in self.data_dict else None
        
    
    # load data
    def load_data(self):
        tagged_methods = load_method_tag_results(self.project.method_tag_output_csv)
        data = [MethodWrapper(tagged_method) for tagged_method in tagged_methods]
        return data

    def save_meta(self, filepath=None):
        def compute_line_stats(data_dict):
            # 每个作者的所有代码项的行数加总
            line_counts = [
                sum(d.get_line_count() for d in items)
                for items in data_dict.values()
            ]
            if not line_counts:
                return {
                    "author_count": 0,
                    "min": 0,
                    "max": 0,
                    "mean": 0,
                    "median": 0
                }
            return {
                "author_count": len(data_dict),
                "min": min(line_counts),
                "max": max(line_counts),
                "mean": round(sum(line_counts) / len(line_counts), 2),
                "median": statistics.median(line_counts)
            }

        train_stats = compute_line_stats(self.training_data)
        trans_stats = compute_line_stats(self.transformation_data)

        # 写入 CSV 文件
        if filepath is None:
            filepath = create_raw_data_meta_csv_path(self.project.name)
        print(filepath)
        with open(filepath, 'w', newline='', encoding='utf-8') as f:
            writer = csv.DictWriter(f, fieldnames=[
                'dataset', 'author_count', 'min', 'max', 'mean', 'median'
            ])
            writer.writeheader()
            writer.writerow({'dataset': 'train', **train_stats})
            writer.writerow({'dataset': 'transformation', **trans_stats})
                
    def filter_common_authors(self):
        forsee_authors = set(self.training_data.keys())
        transformation_authors = set(self.transformation_data.keys())
        common_authors = forsee_authors.intersection(transformation_authors)
        
        return common_authors
    
                    
    def get_author_line_counts(self, data_dict):
        counts = defaultdict(int)
        for author, data_list in data_dict.items():
            for d in data_list:
                counts[author] += d.get_line_count()
        return counts
    
    def filter_common_data(self):
        common_authors = self.filter_common_authors()
        self.transformation_data = {k:self.transformation_data[k] for k in common_authors}
        self.training_data = {k:self.training_data[k] for k in common_authors}
        
    # 去除代码总行数低于min_lines的authors, 去除data_with_tests中不在data_no_tests中的数据
    def clean_data_no_tests(self, min_lines=1):
        line_counts = self.get_author_line_counts(self.training_data)
        sorted_authors = sorted(line_counts.items(), key=lambda x: x[1], reverse=True)
        keep_count = max(1, int(len(sorted_authors) * self.project.top_percent))
        top_authors = set(author for author, _ in sorted_authors[:keep_count])
        
        to_remove = set()
        for author in top_authors:
            if line_counts[author] < min_lines:
                to_remove.add(author)
        top_authors.difference_update(to_remove)
        
        
        self.training_data = {author: data for author, data in self.training_data.items() if author in top_authors}

        
        # 去除transformation src中不在forsee data中的数据
        self.transformation_data = {author:self.transformation_data[author] for author in top_authors if author in self.transformation_data}
        for author in top_authors:
            if author not in self.transformation_data:
                self.transformation_data[author] = []
    
    
    
    def save_data(self, data, csv_path):
        pass
    
    def create_forsee_data(self, output_dir, data, merge_method=False):
        pass

    def get_dominat_author(self):
        dominant_author, dominant_code_size = "", 0
        for author, data in self.data_dict:
            code_size = sum([d.get_line_count() for d in data])
            if code_size > dominant_code_size:
                dominant_author = author
        return dominant_author


# Preprocessed data: Method level data
class MethodData(Data):
    def __init__(self, project_config):
        super().__init__(project_config) 
        self.meta = []

            
    def add_meta(self, group_id, author1, author2, training_data, eval_data):
        # 行数统计（训练集）
        author1_lines_train = sum(d.get_line_count() for d in training_data[author1])
        author2_lines_train = sum(d.get_line_count() for d in training_data[author2])
        # 方法个数统计（评估集）
        author1_methods_eval = len(eval_data[author1])
        author2_methods_eval = len(eval_data[author2])

        # 添加元信息
        self.meta.append({
            "group_id": group_id,
            "author1": author1,
            "author2": author2,
            "author1_lines_train": author1_lines_train,
            "author2_lines_train": author2_lines_train,
            "author1_methods_eval": author1_methods_eval,
            "author2_methods_eval": author2_methods_eval,
        })

    def load_meta(self):
        author_pairs = []
        max_group_id = 0
        filepath = create_raw_data_meta_csv_path(self.project.name)  
        if not os.path.exists(filepath):
            return author_pairs, max_group_id + 1
        with open(filepath, newline='', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                author1 = row['author1']
                author2 = row['author2']
                group_id = int(row['group_id'])

                author_pairs.append((author1, author2))
                max_group_id = max(max_group_id, group_id)
        return author_pairs, max_group_id + 1
    
    

    def save_meta(self, filepath=None):
        if not self.meta:
            print("Meta is empty. Run create_binary_forsee_data first.")
            return

        fieldnames = [
            'group_id',
            'author1', 'author2',
            'author1_lines_train', 'author2_lines_train',
            'author1_methods_eval', 'author2_methods_eval'
        ]

        if not filepath:
            filepath = create_raw_data_meta_csv_path(self.project.name)
        with open(filepath, 'a', newline='', encoding='utf-8') as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(self.meta)
        

    def save_data(self, data, csv_path):
        output_dir = os.path.dirname(csv_path)
        if not os.path.exists(output_dir):
            os.makedirs(output_dir)
        data_list = []
        for tag_result_list in data.values():
            data_list += [{"tag_result":d.instance, "id":d.id} for d in tag_result_list]
        data_list.sort(key=lambda x: int(x["id"][1:]))
        
        titles = list(asdict(data_list[0]["tag_result"]).keys())
        titles.append("id")
        with open(csv_path, 'w', newline='', encoding='utf-8') as csvfile:
            writer = csv.writer(csvfile)
            writer.writerow(titles)

            for result in data_list:
                result_dict = asdict(result["tag_result"])
                result_dict["id"] = result["id"]
                writer.writerow([result_dict[title] for title in titles])   
       
    def create_data(self):
        
        # self.balance_data(self.data_no_tests)
        # self.balance_data(self.data_with_tests)
        
        # 用于forsee训练和评估
        # create_forsee_data(create_forsee_train_codes_dir(self.project.name), self.data_no_tests)
        # create_forsee_data(create_forsee_eval_codes_dir(self.project.name), self.data_with_tests)
        
        
        self.save_data(self.transformation_data, create_tested_data_csv_path(self.project.name))
        self.save_data(self.training_data, create_no_tests_data_csv_path(self.project.name))

        # save_statistic(self.forsee_data, self.project.name, "forsee")
        # save_statistic(self.transormation_data,self.project.name, "transformation-src")



@dataclass
class TransformPair:
    project_name:str
    pair_id:str
    src_author:str
    target_author:str
    src_id:str
    target_ids:List[str]
    
    @classmethod
    def from_dict(cls, data: dict) -> "TransformPair":
        return cls(
            project_name=data["project_name"],
            pair_id=data["pair_id"],
            src_author=data["src_author"],
            target_author=data["target_author"],
            src_id=data["src_id"],
            target_ids=data["target_ids"]
        )
        
        
def collect_lines_stats(data_dict: Dict[str, List[MethodWrapper]]) -> Tuple[int, int, float, float]:
    """
    统计每个作者所有方法代码行数总和，返回 (最小值, 最大值, 平均值, 中位数)
    使用 numpy 进行计算
    """
    lines_counts = []
    for author, results in data_dict.items():
        total_lines = sum(rw.get_line_count() for rw in results)
        lines_counts.append(total_lines)

    if not lines_counts:
        return 0, 0, 0.0, 0.0

    lines_array = np.array(lines_counts)
    return (
        int(np.min(lines_array)),
        int(np.max(lines_array)),
        round(float(np.mean(lines_array)), 2),
        round(float(np.median(lines_array)), 2)
    )
    
    
class Sampler:
    def __init__(self):
        pass
    
    @staticmethod
    def partition(data: List[MethodWrapper], bucket_count: int) -> List[List]:
        data = sorted(data, key=lambda m: m.get_line_count())
        
        # 基于分位数划分
        min_len = data[0].get_line_count()
        max_len = data[-1].get_line_count()
        bucket_size = (max_len - min_len) / bucket_count
        
        # 按桶划分方法
        buckets = [[] for _ in range(bucket_count)]
        for m in data:
            idx = int((m.get_line_count() - min_len) / bucket_size)
            if idx == bucket_count:  # 处理最大值边界情况
                idx = bucket_count - 1
            buckets[idx].append(m)
        

        return buckets

    @staticmethod
    def sample_by_num(data: List[MethodWrapper], sample_num: int, bucket_count, random_seed: int = 42) -> List:
        if len(data) <= sample_num:
            return data
        
        # 先按代码长度排序（升序）
        # data.sort(key=lambda m: m.get_line_count())
        buckets = Sampler.partition(data, bucket_count)
        
        random.seed(random_seed)
        samples = []
        
        # 计算每个桶分配的采样数（均匀）
        sample_per_bucket = sample_num // bucket_count
        reminder = sample_num
        
        for i, bucket in enumerate(buckets):
            if not bucket:
                continue
            if len(bucket) <= sample_per_bucket:
                # 桶内样本不足，全部取
                samples.extend(bucket)
                reminder -= len(bucket)
            else:
                # 从桶内随机采样sample_per_bucket个
                samples.extend(random.sample(bucket, sample_per_bucket))
                reminder -= sample_per_bucket
                
        
        # 如果采样总数不足 sample_num，则从剩余未采样样本补充
        if reminder > 0:
            # 找出未采样的所有样本
            all_samples = [item for bucket in buckets for item in bucket]
            remaining = sorted(list(set(all_samples) - set(samples)), key=lambda m:int(m.id[1:]))
            if len(remaining) < reminder:
                # 不足时全部补充
                samples.extend(remaining)
            else:
                samples.extend(random.sample(remaining, reminder))
            
        
        return samples
    
    
    @staticmethod
    def sample_by_lines(data: List[MethodWrapper], sample_lines: int, bucket_count, random_seed: int = 42) -> List[MethodWrapper]:
        buckets = Sampler.partition(data, bucket_count)
        random.seed(random_seed)

        samples = []
        total_lines = 0
        bucket_index = 0

        # 将每个桶内打乱顺序，确保均匀采样时是随机的
        for bucket in buckets:
            random.shuffle(bucket)

        # 循环采样直到总行数达到目标
        while total_lines < sample_lines:
            # 所有桶都空了，无法再采样
            if all(not bucket for bucket in buckets):
                break

            # 从每个非空桶中采一个方法
            for i in range(bucket_count):
                if buckets[i]:
                    method = buckets[i].pop()
                    samples.append(method)
                    total_lines += method.get_line_count()
                    if total_lines >= sample_lines:
                        break

        return samples
    



class ClassifierResults:
    def __init__(self, project_name):
        self.project_name = project_name
        """
        # key:(author1, author2), 
        # value:{
            author:{
                src_id:{author1:confidence, author2:confidence}
            }
        }

        """
        self.results = {} 

    def load_results(self, filepath=None):
        if filepath is None:
            filepath = create_project_classify_reuslts_path(self.project_name)
        
        if not os.path.exists(filepath):
            return False
        
        with open(filepath, 'r', encoding="utf8") as f:
            for line in f:
                obj = json.loads(line)
                key = tuple(sorted(list(obj.keys())))
                self.results[key] = obj

        return True


    def save_results(self, filepath=None):
        if filepath is None:
            filepath = create_project_classify_reuslts_path(self.project_name)
        os.makedirs(os.path.dirname(filepath), exist_ok=True)
        with open(filepath, 'w', encoding="utf8") as f:
            for k, v in self.results.items():
                f.write(json.dumps(v, ensure_ascii=False) + '\n')


    def generate_results(self, author_pairs, author_data_dict:dict[str, Data]):
        from forsee import run_binary_classifier
        
        self.load_results()

        update = False
        for authors in author_pairs:
            key = tuple(sorted(authors))
            if self.get_reuslt(key):
                continue
            update = True
            for authorname in authors:
                data = author_data_dict[authorname]
                data_dict = {f"{d.id}.java":d.get_code() for d in data.training_data[authorname]}
                data_dict.update({f"{d.id}.java":d.get_code() for d in data.transformation_data[authorname]})
                
                result = run_binary_classifier(self.project_name, *authors, data_dict, authorname )

                key = tuple(sorted(authors))
                if key not in self.results:
                    self.results[key] = {}
                                    
                successful_set = set()
                for filename in data_dict:
                    id = filename.replace(".java", "")
                    classify_result = result.get_result(authorname, id, self.project_name)
                    
                    index = classify_result.author_ids.index(classify_result.author_id)
                    real_author = classify_result.author_name
                    
                    if authorname not in self.results[key]:
                        self.results[key][authorname] = {}
                    
                    if id not in self.results[key][authorname]:
                        self.results[key][authorname][id] = {}
            
                    self.results[key][authorname][id][real_author] = classify_result.confidence_list[index]
                    another_author = [a for a in authors if a != real_author][0]
                    self.results[key][authorname][id][another_author] = classify_result.confidence_list[1 - index]

        if update:
            self.save_results()

    def is_success(self, authors, target_author, src_id):
        key = tuple(sorted(authors))
       
        if key not in self.results:
            return False

        
        confidence_in_target_author = self.results[key][target_author][src_id][target_author]
        another_author = [a for a in authors if a != target_author][0]
        confidence_in_another = self.results[key][target_author][src_id][another_author]
        
        return confidence_in_target_author > confidence_in_another

    def get_confidence(self, authors, target_author, src_id):
        key = tuple(sorted(authors))
        return self.results[key][target_author][src_id][target_author]
    
    def get_confidence_dict(self, authors, author, src_id):
        key = tuple(sorted(authors))
        return self.results[key][author][src_id]
    
    def get_all_results(self):
        return self.results.values()
    
    def get_reuslt(self,  authors):
        key = tuple(sorted(authors))
        return self.results.get(key)
    

    # def norm_all_confidences(self):
    #     def norm_confidence(author_confidence_dict):
    #         authors = list(author_confidence_dict.keys())
    #         scores  = [author_confidence_dict[a] for a in authors]

    #         probs = softmax(scores)
    #         return {a: float(p) for a, p in zip(authors, probs)}
        
    #     for k, v in self.results.items():
    #         for src_id, author_confidence_dict in v.items():
    #             self.results[k][src_id] = norm_confidence(author_confidence_dict)

    #     self.save_results(os.path.join(DATA_DIR, "classify_result", f"{self.project_name}-classify-norm-reuslt.jsonl"))

class TransformationData:
    def __init__(self, project_name, data:Data=None) -> None:
        self.project_name = project_name
        self.data = data
        self.pairs = []
        self.src_count = []
        self.target_count = []
        
    def load_preprocessed_data(self, csv_path):
        results = load_result_wrappers(csv_path)
        data = {}
        for d in results:
            if d.get_dominant_author() not in data:
                data[d.get_dominant_author()] = []
            data[d.get_dominant_author()].append(d)
        return data
    
    @staticmethod
    def load_data(jsonl_path: str) -> list[TransformPair]:
        pairs = []
        with open(jsonl_path, "r", encoding="utf-8") as f:
           for line in f:
               obj = json.loads(line)
               pair = TransformPair.from_dict(obj)
               pairs.append(pair)
        return pairs

    @staticmethod
    def save_data(data: List[TransformPair], output_path: str):
        """
        将 TransformPair 列表保存为 JSONL 文件
        """
        dir = os.path.dirname(output_path)
        os.makedirs(dir, exist_ok=True)
        with open(output_path, "w", encoding="utf-8") as f:
            for pair in data:
                json_line = json.dumps(pair.__dict__, ensure_ascii=False)
                f.write(json_line + "\n")
    
    
    def get_meta(self):
        meta_info = {
            "project": self.project_name,
            "pair_count": len(self.pairs),
            "author_pairs_count": len(self.get_author_pairs()),
            "min_src_method_lines":np.min(self.src_count), 
            "avg_src_method_lines": round(np.mean(self.src_count), 2),
            "median_method_lines":round(np.median(self.src_count), 2),
            "max_src_method_lines":np.max(self.src_count), 
            "avg_target_code_lines": np.mean(self.target_count)
        }
        
        return meta_info


    def save_meta(self, output_path):
        """
        保存：pairs, author pairs, avg lines of src methods, avg lines of target codes
        —— 以 CSV 格式保存，仅一行数据
        """
        if not self.pairs:
            print("No transform pairs found.")
            return


        meta_info = {
            "project": self.project_name,
            "pair_count": len(self.pairs),
            "author_pairs_count": len(self.get_author_pairs()),
            "min_src_method_lines":np.min(self.src_count), 
            "avg_src_method_lines": round(np.mean(self.src_count), 2),
            "median_method_lines":round(np.median(self.src_count), 2),
            "max_src_method_lines":np.max(self.src_count), 
            "avg_target_code_lines": np.mean(self.target_count)
        }

        os.makedirs(os.path.dirname(output_path), exist_ok=True)
        with open(output_path, "w", encoding="utf-8", newline="") as f:
            writer = csv.DictWriter(f, fieldnames=meta_info.keys())
            writer.writeheader()
            writer.writerow(meta_info)


    
    def save_code(self, origin_dir, output_dir, data):
        if os.path.exists(output_dir):
            shutil.rmtree(output_dir)
        for author_name, data_list in data.items():
            data_list:list[MethodWrapper]
            code_dir = os.path.join(output_dir, author_name)
            os.makedirs(code_dir, exist_ok=True)
            for d in data_list:
                src_path = os.path.join(origin_dir,author_name, f"{d.id}.java")
                dst_path = os.path.join(code_dir, f"{d.id}.java")
                shutil.copy(src_path, dst_path)
    
    
    def get_author_pairs(self, print_flag=False):
        classification_reuslt = BatchBinClassificationResult(self.project_name)
        author_pairs = classification_reuslt.get_pairs_by_acc(0.8)
        if print_flag:
            classification_reuslt.print_pairs_by_acc(0.8)
        if not author_pairs:
            print("No binary classification result of authors!")
            return None
        result = [p for p in author_pairs]
        result.extend([(p[1], p[0]) for p in author_pairs])
        return result

    
    def create_target_data(self, authors, min_lines):
        result = {}
        for author in authors:
            # select target codes
            data_list = self.data.training_data[author]
            result[author] = Sampler.sample_by_lines(data_list, min_lines, 10)
        return result
    

    def create_src_data(self, authors, min_src_lines):
        from test import Tester, TestResultParser
        
        # filter data by min_src_lines
        data_dict = {}
        for author in authors: 
            data_dict[author] = [m for m in self.data.transformation_data[author] if m.get_line_count() >= min_src_lines]
        
        
        # Get test result of test classes
        # test_class_test_result = {}
        # for author in authors:
        #     data_list = data_dict[author]
        #     for d in data_list:
        #         if d.get_test_class() not in test_class_test_result:
        #             test_class_test_result[d.get_test_class()] = None
        # for test_class in test_class_test_result:
        #     test_result = Tester.run_test_class(self.project_name, test_class, TestResultParser(), True)
        #     test_class_test_result[test_class] = test_result
            
        # save test result
        # path = create_test_result_path(self.project_name)
        # os.makedirs(os.path.dirname(path), exist_ok=True)
        # with open(path, "w", encoding="utf8") as f:
        #     for result in test_class_test_result.values():
        #         json_obj = json.dumps(asdict(result), ensure_ascii=False)
        #         f.write(json_obj + '\n')
        
        self.src_methods = []
        for author in authors:
            data = [m for m in data_dict[author] if test_class_test_result[m.get_test_class()].has_passed_testcase() ]
            self.src_methods[author] = Sampler.sample_by_num(data, 10, 4)
                
    def filter_src_methods(self, src_author, target_author, min_src_lines):
        # 满足：最低长度要求
        data = [m for m in self.data.transformation_data[src_author] if m.get_line_count() >= min_src_lines]
        
        # 满足：能通过测试
        print("添加测试过滤代码！！")
        # Get test result of test classes
        # test_class_test_result = {}
        # for author in authors:
        #     data_list = data_dict[author]
        #     for d in data_list:
        #         if d.get_test_class() not in test_class_test_result:
        #             test_class_test_result[d.get_test_class()] = None
        # for test_class in test_class_test_result:
        #     test_result = Tester.run_test_class(self.project_name, test_class, TestResultParser(), True)
        #     test_class_test_result[test_class] = test_result
        # # save test result
        # path = create_test_result_path(self.project_name)
        # os.makedirs(os.path.dirname(path), exist_ok=True)
        # with open(path, "w", encoding="utf8") as f:
        #     for result in test_class_test_result.values():
        #         json_obj = json.dumps(asdict(result), ensure_ascii=False)
        #         f.write(json_obj + '\n')
        # data = [m for m in data if test_class_test_result[m.get_test_class()].has_passed_testcase() ]
        
        # 满足:与目标作者风格存在差异
        from forsee import run_binary_classifier
        result = run_binary_classifier(self.project_name, src_author, target_author, {d.id:d.get_code() for d in data}, src_author)
        data = [d for d in data if result.get_result(src_author, d.id).istop1_success()]

        return data
    
    def select_target_codes(self, src_author, target_author, classify_results:ClassifierResults, min_total_lines):
        data_list = self.data.training_data[target_author]
        data_list = [d for d in data_list if classify_results.get_confidence([src_author, target_author], target_author, d.id) >= 0.9]
        return Sampler.sample_by_lines(data_list, min_total_lines, 10)

    
    # create author pairs from project one-by-one
    def create_data(self, min_target_lines, min_src_lines):
        author_pairs = self.get_author_pairs()
        if author_pairs is None:
            return
        

        # 获取不同二分类器的分类结果
        classify_results = ClassifierResults(self.project_name)
        load_success = classify_results.load_results()
        if not load_success:
            authors = set()
            for author1, author2 in author_pairs:
                authors.add(author1)
                authors.add(author2)
            author_data_dict = {author:self.data for author in authors}
            classify_results.generate_results(author_pairs, author_data_dict)
            
        
      
        # 构建pairs
        pair_id = 1
        for pair in author_pairs:
            src_author, target_author = pair
            # filtered_methods = self.filter_src_methods(src_author, target_author, min_src_lines)
            src_methods = Sampler.sample_by_num(self.data.transformation_data[src_author], 10, 4)
            target_codes = self.select_target_codes(src_author, target_author,classify_results, min_target_lines)
            for m in src_methods:
                self.pairs.append(TransformPair(self.project_name, str(pair_id), src_author, target_author, m.id, [d.id for d in target_codes]))
                pair_id += 1

            # 添加meta
            self.src_count.extend([m.get_line_count() for m in src_methods])
            self.target_count.append(sum([m.get_line_count() for m in target_codes]))
        
        return self.pairs
    

class TransformationDataAcrossProject(TransformationData):
    
    def __init__(self, project_name: str, data: Data):
        super().__init__(project_name, data)
        self.project_data_dict = dict[str, Data]()

        
    def filter_data(self, min_lines_of_total, min_lines_of_single):
        new_project_data_dict = {}
        for project, data in self.project_data_dict.items():
            new_data_dict = {}
            for author, data_list in data.data_dict.items():
                data_list = [d for d in data.data_dict[author] if d.get_only_code_lines_count() >= min_lines_of_single]
                total_lines = sum([d.get_only_code_lines_count() for d in data_list])
                if total_lines >= min_lines_of_total:
                    new_data_dict[author] = data_list
            data.update_data(new_data_dict)
            if data.data_dict:
                new_project_data_dict[project] = data
        self.project_data_dict = new_project_data_dict
        
    def get_all_authors(self):
        authors = []
        for project, data in self.project_data_dict.items():
            for author, data_list in data.data_dict.items():
                authors.append(author)

        return authors
    
    
    def split_author_name(self, author_name)->Data:
        for project, data in self.project_data_dict:
            prefix = f"{project}-"
            if author_name.startswith(prefix):
                return project, author_name.replace(prefix, "")
        return None, None
    
    def save_codes(self):
        author_pairs = self.get_author_pairs()
        
                        
        # 保存相关作者的所有代码
        authors = set()
        for p in author_pairs:
            authors.add(p[0])
            authors.add(p[1])
        codes_dir = create_codes_dir(self.project_name)

        if os.path.exists(codes_dir):
            shutil.rmtree(codes_dir)

        for project, data in self.project_data_dict.items():
            dict_list = [data.training_data, data.transformation_data]
            for dict in dict_list:
                for author, data_list in dict.items():
                    if author not in authors:
                        continue
                    
                    author_dir = os.path.join(codes_dir, author)
                    os.makedirs(author_dir, exist_ok=True)
                    for d in data_list:
                        filepath = os.path.join(author_dir, f"{d.id}.java")
                        if os.path.exists(filepath):
                            continue
                        with open(filepath, 'w', encoding="utf8") as f:
                            f.write(d.get_code())

# `min_lines_of_total`: minimum lines of an author's all codes
    def create_data(self, min_target_codes, sample_num_per_author_pair, min_lines_of_single, min_lines_of_total):
        for project in ProjectConfigs().projects:
            self.project_data_dict[project.name] = Data(project)
            
        # 创建forsee训练和测试集
        self.filter_data(min_lines_of_total, min_lines_of_single)
        authors = self.get_all_authors()       
        data_dict = {author:self.project_data_dict[AUTHOR_ID_MAP.get_author_project(author)] for author in authors}
        output_dir = create_forsee_train_codes_dir(self.project_name)
        author_pairs = create_binary_forsee_data(data_dict, output_dir)
        
        # 训练分类器，并对所有数据进行分类
        path = create_project_classify_reuslts_path(self.project_name)
        bin_classify_results = BatchBinClassificationResult(self.project_name)
        bin_classify_results.load(create_forsee_bin_classification_acc_path(self.project_name))
        # 检查是否所有分类器分类完毕
        need_train = False
        classifier_manger = ClassifierManager()
        for pair in author_pairs:
            create_forsee_eval_path(self.project_name)
            basename = os.path.basename(classifier_manger.get_classifier_dirname(self.project_name, list(pair)))
            eval_path = create_forsee_eval_path(basename)
            # 没有测试结果，分类器训练阶段可能出错，移除缓存重新训练
            if not os.path.exists(eval_path):
                cache_dir = classifier_manger.get_cache_dir(self.project_name, list(pair))
                print(f"Remove cache dir:{cache_dir}")
                shutil.rmtree(cache_dir, ignore_errors=True)
                need_train = True
        if need_train:
            subprocess.run(['bash', 'train_forsee.sh', '^across-project[0-9]+$'])

        # self.save_codes()
            
        # 获取可用作者对
        author_pairs =self.get_author_pairs(True)
        
        # 获取所有数据在不同分类器上的结果
        classify_results = ClassifierResults(self.project_name)
        author_data_dict = {author:self.project_data_dict[AUTHOR_ID_MAP.get_author_project(author)] for author in authors}
        classify_results.generate_results(author_pairs, author_data_dict)
            
        
        # 构建pairs
        pair_id = 1
        for pair in author_pairs:
            src_author, target_author = pair
            # filtered_methods = self.filter_src_methods(src_author, target_author, min_src_lines)
            src_project = AUTHOR_ID_MAP.get_author_project(src_author)
            target_project = AUTHOR_ID_MAP.get_author_project(target_author)
            src_methods = Sampler.sample_by_num(self.project_data_dict[src_project].transformation_data[src_author], sample_num_per_author_pair, 4)
            
            data_list = self.project_data_dict[target_project].training_data[target_author]
            data_list = [d for d in data_list if classify_results.get_confidence([src_author, target_author], target_author, d.id) >= 0.9]
            target_codes = Sampler.sample_by_lines(data_list, min_target_codes, 10)
            for m in src_methods:
                self.pairs.append(TransformPair(self.project_name, str(pair_id), src_author, target_author, m.id, [d.id for d in target_codes]))
                pair_id += 1
                
            # 添加meta
            self.src_count.extend([m.get_line_count() for m in src_methods])
            self.target_count.append(sum([m.get_line_count() for m in target_codes]))
               
        return self.pairs


def create_binary_classification_data():
    configs = ProjectConfigs(r'projects-config.json')
    for project_config in configs.projects:
        data = MethodData(project_config)
        data_dict = {author:data for author in data.data_dict}
        output_dir = create_forsee_train_codes_dir(data.project.name)
        create_binary_forsee_data(data_dict, output_dir)
        data.save_meta()
        print(project_config.name)


def save_codes():
        
    configs = ProjectConfigs(r'projects-config.json')
    for project_config in configs.projects:
        data = Data(project_config)
       
        transform_data = TransformationData(project_config.name, data)
        author_pairs = transform_data.get_author_pairs()
        
                        
        # 保存相关作者的所有代码
        authors = set()
        for p in author_pairs:
            authors.add(p[0])
            authors.add(p[1])
        codes_dir = create_codes_dir(transform_data.project_name)

        if os.path.exists(codes_dir):
            shutil.rmtree(codes_dir)

        dict_list = [transform_data.data.training_data, transform_data.data.transformation_data]
        for dict in dict_list:
            for author, data_list in dict.items():
                if author not in authors:
                    continue
                
                author_dir = os.path.join(codes_dir, author)
                os.makedirs(author_dir, exist_ok=True)
                for d in data_list:
                    filepath = os.path.join(author_dir, f"{d.id}.java")
                    if os.path.exists(filepath):
                        continue
                    with open(filepath, 'w', encoding="utf8") as f:
                        f.write(d.get_code())
        


class MetaData:
    def save_pair_meta(output_path, meta_infos: list[dict]):
        os.makedirs(os.path.dirname(output_path), exist_ok=True)

        # 获取字段名（假设所有 dict 都一致）
        fieldnames = list(meta_infos[0].keys())

        # 写入 CSV 文件
        with open(output_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()

            # 写入每个项目的 meta 信息
            for meta in meta_infos:
                writer.writerow(meta)

            # 计算汇总（平均或总和）
            combined_meta = {'project': 'ALL'}
            for key in fieldnames:
                if key == 'project':
                    continue
                values = [meta[key] for meta in meta_infos]
                if all(isinstance(v, int) for v in values):
                    combined_meta[key] = int(np.sum(values))  # 整数合并用 sum
                else:
                    combined_meta[key] = round(float(np.mean(values)), 2)  # 浮点平均

            writer.writerow(combined_meta)


def create_transform_pairs(min_target_code_lines):
    min_target_code_lines = int(min_target_code_lines)
    # 构建transform pair
    configs = ProjectConfigs(r'projects-config.json')
    meta_infos = []
    for project_config in configs.projects:
        data = Data(project_config)
        transform_data = TransformationData(project_config.name, data)
        pairs = transform_data.create_data(min_target_code_lines, 10)
        TransformationData.save_data(transform_data.pairs, create_transformation_pair_path(transform_data.project_name, min_target_code_lines))
        meta_infos.append(transform_data.get_meta())
        
    MetaData.save_pair_meta(create_pairs_meta_path("", min_target_code_lines), meta_infos)
        
def create_transform_pairs_across_project(min_target_code_lines):
    min_target_code_lines = int(min_target_code_lines)
    # 构建transform pair
    meta_infos = []
    transform_data = TransformationDataAcrossProject("across-project", None)
    pairs = transform_data.create_data(min_target_code_lines, 10, 20, 1200)
    TransformationData.save_data(transform_data.pairs, create_transformation_pair_path(transform_data.project_name, min_target_code_lines))
    meta_infos.append(transform_data.get_meta())
    
    
    MetaData.save_pair_meta(create_pairs_meta_path("", min_target_code_lines), meta_infos)

    
if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("Usage: python dataset.py <arg>")
        sys.exit(1)

    random.seed(42)
    fire.Fire()
    
        