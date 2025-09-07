import csv
import json
import numpy as np

from scipy.special import softmax

from dataclasses import dataclass, fields, asdict
from typing import List, Dict
from collections import Counter

from dataset import TransformationData, ClassifierResults, TransformPair
from config import ProjectConfigs
from transform import ResultManager, DataCache
from utils import *
from forsee import (
    classify_transformed_results, 
    ClassificationResultCollection, 
    ClassificationResult
)


class TranformType:
    SUCCESS = "Success"
    FAILURE = "Failure"
    ALIGNED = "Aligned"
    REVERSAl = "Reversal"
    FAILURE_MODIFICATION = "Failure-Modification"
    FAILURE_NO_MODIFICATION = "Failure-No-Modification"


    @staticmethod
    def get_type(labels:list[bool]):
        if labels == [True, True, True]:
            return TranformType.SUCCESS
        elif labels == [True, True, False]:
            return TranformType.FAILURE
        elif labels == [False, True, True]:
            return TranformType.ALIGNED
        elif labels == [False, True, False]:
            return TranformType.REVERSAl        
        
    @staticmethod
    def get_failure_type(original_code, result_code):
        if original_code.strip('\r\n') == result_code.strip('\r\n'):
            return TranformType.FAILURE_NO_MODIFICATION
        else:
            return TranformType.FAILURE_MODIFICATION



def get_confidence_in_authors(classify_result:ClassificationResult, authors):
    author_confidence_dict = {}
        
    index = classify_result.author_ids.index(classify_result.author_id)
    real_author = classify_result.author_name
    author_confidence_dict[real_author] = classify_result.confidence_list[index]

    another_author = [a for a in authors if a != real_author][0]
    author_confidence_dict[another_author] = classify_result.confidence_list[1 - index]
    
    return author_confidence_dict

def norm_confidence(author_confidence_dict):
    authors = list(author_confidence_dict.keys())
    scores  = [author_confidence_dict[a] for a in authors]

    probs = softmax(scores)
    return {a: float(p) for a, p in zip(authors, probs)}

def create_pair_dict(min_target_lines, project_names) -> Dict[tuple,TransformationData]:
    pair_dict = {}
    for project_name in project_names:
        pair_path = create_transformation_pair_path(project_name, min_target_lines)
        pairs = TransformationData.load_data(pair_path)
        pair_dict.update({(pair.project_name, pair.pair_id): pair for pair in pairs})
    return pair_dict

class EvalResults:
    def __init__(self, method, min_target_lines, across_project_flag=False):
        self.min_target_lines = min_target_lines
        self.method = method
        self.results = []
        self.search_index = None # key:(project_name, pair_id), value:index in self.results
        self.across_project_flag = across_project_flag
        
        
        self.__load_results()
        
    def __load_results(self):
        eval_results_path = create_eval_results_path(self.method, self.min_target_lines)
        # if os.path.exists(eval_results_path):
        #     self.load_from(eval_results_path)
        #     return
        
        if self.across_project_flag:
            project_names = ["across-project"]
        else:
            project_names = [project_name for project_name in ProjectConfigs().get_all_project_names()]

        pair_dict = create_pair_dict(self.min_target_lines, project_names)
        
        result_path = create_transformation_result_jsonl_path(self.method, self.min_target_lines)
        transform_results = ResultManager(result_path)

        # 获取转换后的代码的forsee分类结果
        transformed_classify_results = classify_transformed_results(self.method, self.min_target_lines)
        
        original_classify_dict = {}

        for r in transform_results.get_all_results():
            project_name = r.project_name
            pair = pair_dict[(r.project_name, r.pair_id)]
            src_author, target_author = pair.src_author, pair.target_author

            transform_classify = transformed_classify_results.get_result(target_author, r.src_id, r.project_name, src_author)

            if transform_classify is None:
                print(f"No classification result was found of transformation result.")
                continue
            
            # 添加原始数据的分类结果
            if r.project_name not in original_classify_dict:
                original_classify_result = ClassifierResults(r.project_name)
                if not original_classify_result.load_results():
                    print("No classify results of dataset have been found!")
                original_classify_dict[r.project_name] = original_classify_result
            
            # 计算labels
            transformed_label_belong_to_target = transform_classify.istop1_success()
            if  original_classify_dict[r.project_name] is None:
                print(f"{r.project_name}, ")
            src_label_belong_to_src =  original_classify_dict[r.project_name].is_success([src_author,target_author], src_author, r.src_id)
            classify_labels = [src_label_belong_to_src, True, transformed_label_belong_to_target]
            
            # 计算tranform_type
            tranform_type = TranformType.get_type(classify_labels)
            if tranform_type == TranformType.FAILURE:
                original_code = DataCache.get_code_list(project_name, src_author, [r.src_id])[0]
                tranform_type = TranformType.get_failure_type(original_code, r.code)

            # 计算置信度变化
            confidence_dict = original_classify_dict[r.project_name].get_confidence_dict([src_author, target_author],src_author, r.src_id)
            confidence_dict1 = get_confidence_in_authors(transform_classify, [src_author, target_author]) # transformed code
            confidence_dict = norm_confidence(confidence_dict)
            confidence_dict1 = norm_confidence(confidence_dict1)
            confidence_change = (confidence_dict1[target_author] - confidence_dict[target_author])

            self.results.append(EvalResults.EvalResult(
                project_name=project_name,
                pair_id=r.pair_id,
                labels=classify_labels,
                transform_type=tranform_type,
                original_confidence=confidence_dict,
                transformed_confidence=confidence_dict1,
                confidence_change=confidence_change
            ))
            
        # self.save_to_jsonl(eval_results_path)
        # self.save_to_csv(eval_results_path)
        
            
            
    def load_from(self, path: str):
        self.results = []
        with open(path, 'r', encoding='utf-8') as f:
            for line in f:
                data = json.loads(line.strip())
                self.results.append(EvalResults.EvalResult.from_dict(data))

            
    def get_transform_type_distribution(self):

        total = len(self.results)
        type_counts = Counter([r.transform_type for r in self.results])

        # 计算百分比，保留两位小数
        type_percentages = {
            k: round(v / total * 100, 2) for k, v in type_counts.items()
        }

        return type_percentages
    
    
    def save_to_jsonl(self, output_path):
        os.makedirs(os.path.dirname(output_path), exist_ok=True)
        with open(output_path, 'w', encoding='utf-8') as f:
            for result in self.results:
                json.dump(asdict(result), f, ensure_ascii=False)
                f.write('\n')


    def save_to_csv(self, output_path):
        os.makedirs(os.path.dirname(output_path), exist_ok=True)

        with open(output_path, 'w', encoding='utf-8', newline='') as f:
            writer = csv.DictWriter(f, fieldnames=[
                "project_name", "pairID", "src_author", "target_author",
                "label_src", "label_target", "label_transformed",
                "transform_type",
                "confidence_0", "confidence_1", "confidence_2",
                "confidence_change"
            ])
            writer.writeheader()

            for result in self.results:
                row = {
                    "project_name": result.project_name,
                    "pairID": result.pairID,
                    "src_author": result.src_author,
                    "target_author": result.target_author,
                    "label_src": result.labels[0],
                    "label_target": result.labels[1],
                    "label_transformed": result.labels[2],
                    "transform_type": result.transform_type,
                    "confidence_0": result.norm_confidence_list[0],
                    "confidence_1": result.norm_confidence_list[1],
                    "confidence_2": result.norm_confidence_list[2],
                    "confidence_change": result.confidence_change
                }
                writer.writerow(row)


    def get_failures(self):
        return [r for r in self.results if TranformType.get_type(r.labels) == TranformType.FAILURE]
    
    def get_successes(self):
        return [r for r in self.results if r.transform_type == TranformType.SUCCESS]
    
    def get_failure_modifications(self):
        return [r for r in self.results if r.transform_type == TranformType.FAILURE_MODIFICATION]
    
    
    
    @dataclass
    class EvalResult:
        project_name:str
        pair_id: str
        labels: List[bool]  # [belong_to_src_label(src_code), belong_to_target_label(target_code),belong_to_target_label(transformed_label) ]
        transform_type: str  # Success, Failure, Reversal, Aligned
        original_confidence:Dict[str, float]
        transformed_confidence: Dict[str, float]
        confidence_change: float=-1
        
        @staticmethod
        def from_dict(d):
            valid_fields = {f.name for f in fields(EvalResults.EvalResult)}
            # 过滤掉多余字段，只保留合法字段
            filtered_data = {k: v for k, v in d.items() if k in valid_fields}
            return EvalResults.EvalResult(**filtered_data)
        
    def get_result(self, project_name, pair_id) -> EvalResult:
        if self.search_index is None:
            self.search_index = {}
            for index, r in enumerate(self.results):
                self.search_index[(r.project_name, r.pair_id)] = index
        if (project_name, pair_id) not in self.search_index:
            return None
        return self.results[self.search_index[(project_name, pair_id)]]
        

# 输出每种方法在每种transform types上的占比到一个csv文件


def output_transform_type_distribution_table(eval_result_list: List[EvalResults], output_path: str):
    # 获取所有 transform_type
    all_types = set()
    method_to_distribution = {}

    for er in eval_result_list:
        dist = er.get_transform_type_distribution()
        method_to_distribution[er.method] = dist
        all_types.update(dist.keys())

    all_types = sorted(all_types)  # 固定列顺序

    with open(output_path, "w", newline='') as f:
        writer = csv.writer(f)
        # 写表头
        header = ["Method"] + all_types
        writer.writerow(header)

        # 写每行
        for method, dist in method_to_distribution.items():
            row = [method] + [dist.get(t, 0.0) for t in all_types]
            writer.writerow(row)

# 返回低质量pair
def eval_target_quality(min_target_lines):
    crs_dict:Dict[str, ClassifierResults] = {}
    pairs:list[TransformPair] = []

    for project_name in ProjectConfigs().get_all_project_names():
        crs = ClassifierResults(project_name)
        if not crs.load_results():
            crs.generate_results()
        crs_dict[project_name] = crs

        pair_path = create_transformation_pair_path(project_name, min_target_lines)
        pairs.extend(TransformationData.load_data(pair_path))


    # 计算所有pair的target的最小置信度，平均置信度
    low_quality_pairs = []
    min_confidences, avg_confidences = [], []
    confidence_dict = {}
    for p in pairs:
        key = (p.project_name, p.pair_id)
        confidences = []
        for fileid in p.target_ids:
            confidences.append(crs_dict[p.project_name].get_confidence_dict([p.src_author, p.target_author], p.target_author, fileid)[p.target_author])
        minc, avgc = np.min(confidences), np.mean(confidences)
        min_confidences.append(minc)
        avg_confidences.append(avgc)
        if minc < 0.9:
            low_quality_pairs.append(p)

        confidence_dict[(p.project_name, p.pair_id)] = avgc
            
    # 将低质量pair的信息输出到jsonl文件中
    output_path = create_transformation_pair_path("low-quality", min_target_lines)
    with open(output_path, "w", encoding="utf8") as f:
        for p in low_quality_pairs:
            f.write(json.dumps(asdict(p))+"\n")

    return confidence_dict

# 返回高质量pair
def get_high_quality_src(min_target_lines):
    crs_dict:Dict[str, ClassifierResults] = {}
    pairs:list[TransformPair] = []

    for project_name in ProjectConfigs().get_all_project_names():
        crs = ClassifierResults(project_name)
        if not crs.load_results():
            crs.generate_results()
        crs_dict[project_name] = crs

        pair_path = create_transformation_pair_path(project_name, min_target_lines)
        pairs.extend(TransformationData.load_data(pair_path))


    # 计算所有pair的target的最小置信度，平均置信度
    high_quality_pairs = []
    min_confidences, avg_confidences = [], []
    confidence_dict = {}
    for p in pairs:
        key = (p.project_name, p.pair_id)
        confidence = crs_dict[p.project_name].get_confidence_dict([p.src_author, p.target_author],p.src_author, p.src_id)[p.src_author]
        if confidence >= 0.9:
            high_quality_pairs.append(p)

    return high_quality_pairs


# 计算给定的pairs中src和target代码在target作者上的置信度差值
def cal_src_target_style_distance(min_target_lines, pairs:list[TransformPair]):
    target_confidence = eval_target_quality(min_target_lines)

    crs_dict:Dict[str, ClassifierResults] = {}

    for project_name in ProjectConfigs().get_all_project_names():
        crs = ClassifierResults(project_name)
        if not crs.load_results():
            crs.generate_results()
        crs_dict[project_name] = crs

    diff = []
    for p in pairs:
        confidence_in_target = crs_dict[p.project_name].get_confidence_dict([p.src_author, p.target_author], p.src_author, p.src_id)[p.target_author]
        diff.append(target_confidence[(p.project_name, p.pair_id)] - confidence_in_target)
    return np.mean(diff)

def eval_src_quality(method, min_target_lines):
    ers = EvalResults(method, min_target_lines)

    pairs = []
    for project_name in ProjectConfigs().get_all_project_names():
        pair_path = create_transformation_pair_path(project_name, min_target_lines)
        pairs.extend(TransformationData.load_data(pair_path))

    failure_keys = [(r.project_name, r.pair_id) for r in ers.get_failures()]
    failure_pairs = [p for p in pairs if (p.project_name, p.pair_id) in failure_keys]
    failure_diff = cal_src_target_style_distance(min_target_lines, failure_pairs)

    success_keys = [(r.project_name, r.pair_id) for r in ers.get_successes()]
    success_pairs = [p for p in pairs if (p.project_name, p.pair_id) in success_keys]
    success_diff = cal_src_target_style_distance(min_target_lines, success_pairs)

    print(f"{method}\nstyle distance in failures:{failure_diff:.3f}\nstyle distance in successes: {success_diff:.3f}\n")

# total: Success + Failure-Modification
def output_success_rate_table(eval_result_list: List[EvalResults], output_path: str):
    method_to_distribution = {}
    for er in eval_result_list:
        dist = {}
        dist[TranformType.FAILURE] = len(er.get_failures())
        dist[TranformType.SUCCESS] = len(er.get_successes())
        method_to_distribution[er.method] = dist

    success_rate_dict = {}
    for m, dist in method_to_distribution.items():
        total = dist[TranformType.SUCCESS] + dist[TranformType.FAILURE]
        if total == 0:
            success_rate_dict[m] = "-"
        else:
            success_rate_dict[m] = round(dist[TranformType.SUCCESS] / total * 100, 2)

    with open(output_path, "w", newline='') as f:
        writer = csv.writer(f)
        # 写表头
        header = ["Method", "Success Rate"]
        writer.writerow(header)

        # 写每行
        for method, success_rate in success_rate_dict.items():
            row = [method, success_rate]
            writer.writerow(row)


def eval_transformers(across_project_flag=False):
    methods = [
        "egsi",
        # "egsi-newformat",
        # "egsi-lexical",
        # "egsi-syntactic",
        # "egsi-format",
        # "egsi-naming",
        # "egsi-structure",
        "codebuff",
        "deepseek-r1-0528--free",
        "gpt-4.1",
        "claude-3.7-sonnet"
    ]
    
    import sys
    min_target_lines = int(sys.argv[1])
    eval_results_list = [EvalResults(m, min_target_lines, across_project_flag) for m in methods]

    # 过滤掉target质量低的数据
    # pair_path = create_transformation_pair_path("low-quality", min_target_lines)
    # if os.path.exists(pair_path):
    #     low_quality_pairs = {}
    #     low_quality_pairs.update({(pair.project_name, pair.pair_id): pair for pair in TransformationData.load_data(pair_path)})
    #     for ers in eval_results_list:
    #         ers.results = [r for r in ers.results if (r.project_name, r.pair_id) not in low_quality_pairs]

    # 过滤掉src质量低的数据
    # high_quality_pairs = [(r.project_name, r.pair_id) for r in get_high_quality_src(min_target_lines)]
    # for ers in eval_results_list:
    #     ers.results = [r for r in ers.results if (r.project_name, r.pair_id) in high_quality_pairs]
    
    for e in eval_results_list:
        print(f"{e.method}:{len(e.results)}")
    output_transform_type_distribution_table(eval_results_list, create_eval_transform_types_path(min_target_lines))
    output_success_rate_table(eval_results_list, create_eval_transform_success_rate_path(min_target_lines))


if __name__ == "__main__":
    eval_transformers(across_project_flag=True)

    # eval_target_quality(200)
    # eval_src_quality("egsi", 200)