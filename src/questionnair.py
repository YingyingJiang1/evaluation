import math
import os
import re
import shutil
import json
from collections import defaultdict, Counter
from pathlib import Path
from typing import Dict, Tuple
import numpy as np
import random
from codebleu import calc_codebleu
from upsetplot import UpSet, from_contents
from itertools import product
import plotly.graph_objects as go


from transform import ResultManager, TransformReuslt
from dataset import TransformationData, TransformPair
from path import *
from utils import *
from config import ProjectConfigs
from eval import (
    EvalResults, create_pair_dict, DataCache, TranformType,
    create_pair_dict,ClassifierResults
)

"""
Structure of directory:
target author
    | src-codes
        | src author
            | src id
                | original src file
                | transformation type
                    | transformation results
    | style-codes
"""
def save_code(methods, min_target_codes, project_names,
                output_dir, pair_dict, condition):
    
    # A -> src code, Target -> target-style code
    anonymous_methods = {
            "egsi":"B",
            "codebuff":"C",
            "deepseek-r1-0528--free":"D",
            "gpt-4.1":"E",
            "claude-3.7-sonnet":"F"
    }

    if os.path.exists(output_dir):
        shutil.rmtree(output_dir)
        
    # key:method, value:{"success":0, "failure":"", "lines_median":[]}
    stats = {} 
    for method in methods:
        stats[method] = {"lines_median":[]}
        
    classification_results_map = {}
    
    
    target_map = {}
    with open(os.path.join(HUMAN_STUDY_DIR, 'tasks.jsonl'), 'r') as f:
        tasks = [json.loads(line) for line in f]
    for t in tasks:
        key = t["target_author"] + t["src_author"] + t["src_id"]
        target_map[key] = t["target_ids"]
    # print(target_map)
    
    style_dir_map = {}

    random.seed(42)
    for method in methods:
        eval_results = EvalResults(method, min_target_codes, "across-project" in project_names) 
        transform_results = ResultManager(create_transformation_result_jsonl_path(method, min_target_codes))
        for r in eval_results.results:
            if condition(r):
                if (r.project_name, r.pair_id) not in pair_dict:
                    continue
                # update statistics
                if r.transform_type not in stats[method]:
                    stats[method][r.transform_type] = 0
                stats[method][r.transform_type] += 1
                stats[method]["lines_median"].append(len(transform_results.get_result(r.project_name, r.pair_id).code.splitlines()))
                
                pair =  pair_dict[(r.project_name, r.pair_id)]
                src_author, target_author, src_id =pair.src_author, pair.target_author, pair.src_id
                target_ids = pair.target_ids
                style_value = target_author + target_ids.__str__()
                if style_value not in style_dir_map:
                    style_dir_map[style_value] = f"style{len(style_dir_map)}"
                pair_dir = os.path.join(output_dir,  f"{target_author}-{''.join(target_ids)}")
                                        
                # write transformation code and original src file
                src_codes_dir = os.path.join(pair_dir, src_author, src_id)
                os.makedirs(src_codes_dir, exist_ok=True)
                code = transform_results.get_result(r.project_name, r.pair_id).code
                if not code:
                    code = DataCache.get_code_list(r.project_name, src_author, [src_id])[0]
                dir = os.path.join(src_codes_dir,  r.transform_type)
                # os.makedirs(dir, exist_ok=True)
                result_file_name = method
                with open(os.path.join(src_codes_dir, f"{result_file_name}.java"), 'w') as f:
                    f.write(code)
                original_src_code = DataCache.get_code_list(r.project_name, src_author, [src_id])[0]
                src_filename = "original_src"
                with open(os.path.join(src_codes_dir, f"{src_filename}.java"), 'w') as f:
                    f.write(original_src_code)

                # write style examples
                if r.project_name not in classification_results_map:
                    classifier_reuslts = ClassifierResults(r.project_name)
                    classifier_reuslts.load_results()
                    classification_results_map[r.project_name] = classifier_reuslts
                src_ids = pair_dict[(r.project_name, r.pair_id)].target_ids
                
                style_codes_dir = os.path.join(pair_dir,"style-codes")
                if not os.path.exists(style_codes_dir):
                    os.makedirs(style_codes_dir, exist_ok=True)
                    code_list = DataCache.get_code_list(r.project_name, target_author, src_ids)
                    for i in range(len(src_ids)):
                        with open(os.path.join(style_codes_dir, f"{src_ids[i]}.java"), 'w') as f:
                            f.write(code_list[i])
                
                key = f"{target_author}{src_author}{src_id}"
                if key in target_map :
                    src_ids = target_map[key] 
                
                target_path = os.path.join(src_codes_dir, "Target.java")
                if not os.path.exists(target_path):
                    for id in src_ids:
                        code_list = DataCache.get_code_list(r.project_name, target_author, [id])
                        with open(target_path, 'a') as f:
                            # f.write(f"// {id}\n")
                            f.write(code_list[0])
                            f.write("\n\n")
                
                
                            
        stats[method]["lines_median"] = np.median(stats[method]["lines_median"])
    stats["total_task"] = len(pair_dict)
    style_combinations = set()
    ordered_sytle_combinations = set()
    for p in pair_dict.values():
        authors = [p.src_author, p.target_author]
        style_combinations.add(tuple(sorted(authors)))
        ordered_sytle_combinations.add(tuple(authors))
    stats["style_combinations"] = len(style_combinations)
    stats["ordered_sytle_combinations"] = len(ordered_sytle_combinations)
    print(stats)
                
                
def create_structure_dir(min_target_codes, output_dir, pairs, methods):
    from eval import EvalResults
    
    result_dict = {}
    code_map = {}
    for m in methods:
        result_dict[m] = EvalResults(m, min_target_codes)
        result_path = create_transformation_result_jsonl_path(m, min_target_codes)
        code_map[m] = ResultManager(result_path)
    
    pairs:list[TransformPair]
    for p in pairs:
        project_name = p.project_name
        # 复制target style examples
        for id in p.target_ids:
            src_path = os.path.join(create_codes_dir(p.project_name), p.target_author, id + ".java")
            dst_path = os.path.join(output_dir, p.project_name, p.target_author,"style-codes", id + ".java")
            os.makedirs(os.path.dirname(dst_path), exist_ok=True)
            shutil.copy2(src_path, dst_path)
            
        src_dir = os.path.join(output_dir, p.project_name, p.target_author,"src-codes", p.src_author, p.src_id)
        os.makedirs(src_dir, exist_ok=True)
        
        # 复制原始src文件
        src_path = os.path.join(create_codes_dir(project_name), p.src_author, p.src_id + ".java")
        dst_path = os.path.join(src_dir, "origin.java")
        shutil.copy2(src_path, dst_path)

        for method_name, results in result_dict.items():
            dir = os.path.join(src_dir, results.get_result(p.project_name, p.pair_id).transform_type)
            os.makedirs(dir, exist_ok=True)
            with open(os.path.join(dir, f"{method_name}.java"), 'w') as f:
                code = code_map[method_name].get_result(p.project_name, p.pair_id).code
                f.write(code)
           
def is_success(transform_type):
    from eval import TranformType
    
    return transform_type == TranformType.SUCCESS

def is_faulure(transform_type):
    from eval import TranformType
    return transform_type == TranformType.FAILURE_MODIFICATION or transform_type == TranformType.FAILURE_NO_MODIFICATION


# return pairs that better_method success but worse_method failure.
def filter_pairs_by_comparation(min_target_codes, better_method, worse_method, pairs):
    from eval import EvalResults, TranformType
    
        
    better_results = EvalResults(better_method, min_target_codes)
    worse_results = EvalResults(worse_method, min_target_codes)
    
    return [p for p in pairs if is_success(better_results.get_result(p.project_name, p.pair_id).transform_type)
            and is_faulure(worse_results.get_result(p.project_name, p.pair_id).transform_type)]

            

# methods = ["egsi",
#         #    "deepseek-r1-0528--free"
#            ]
# min_target_codes = 200
# project_name = "jedis"
# pair_file = create_transformation_pair_path(project_name, min_target_codes)

# save_code(methods ,min_target_codes, "jedis", pair_file, "failure", lambda r:r.successful_trans == False)
# save_code(methods ,min_target_codes, "jedis", pair_file, "test-failure", lambda r:r.test_passed == "")


def compare_result(m1, m2, min_target_codes):
    result_path = create_transformation_result_jsonl_path(m1, min_target_codes)
    result_manager1 = ResultManager(result_path)
    result_path = create_transformation_result_jsonl_path(m2, min_target_codes)
    result_manager2 = ResultManager(result_path)
    
    keys = result_manager1.result_dict.keys()
    not_same_count = 0
    for k in keys:
        if result_manager1.result_dict[k].code != result_manager2.result_dict[k].code:
            not_same_count += 1
            
    print(f"Not same count:{not_same_count}")


class EgsiRunningStat:
    def __init__(self):
        self.data: Dict[Tuple[str, str, str], Dict] = {}

    def add_from(self, path: str):
        with open(path, 'r', encoding='utf-8') as f:
            for line in f:
                obj = json.loads(line)
                key = obj['key']
                k = (key['srcAuthor'], key['targetAuthor'], key['srcFileID'])
                self.data[k] = obj['statistic']

    def cal_all_styles_freq(self, keys):
        total_counter = Counter()
        for key in keys:
            statistic = self.data.get(key)
            if not statistic:
                continue
            style_freq = statistic.get('styleFreq', {})
            total_counter.update(style_freq)
        return dict(total_counter)

    def all_keys(self):
        return self.data.keys()

    def all_entries(self):
        for key, statistic in self.data.items():
            yield {
                'srcAuthor': key[0],
                'targetAuthor': key[1],
                'srcFileID': key[2],
                'styleFreq': statistic.get('styleFreq', {}),
                'structureIdFreq': statistic.get('structureIdFreq', {})
            }
            
    def get_executed_op_types(self, src_author, target_author, src_id):
        key = (src_author, target_author, src_id)
        return len(self.data[key]["styleFreq"])


import matplotlib.pyplot as plt
import matplotlib

def draw_pie(freq_dict, title, output_path, label_colors):
    if not freq_dict:
        print("No data available for the given keys.")
        return

    labels = list(freq_dict.keys())
    sizes = list(freq_dict.values())

    # 使用提供的颜色字典，若某 label 没有指定颜色，则使用默认颜色
    colors = [label_colors.get(label, None) for label in labels]

    plt.figure(figsize=(8, 8))
    plt.pie(sizes, labels=labels, autopct='%1.1f%%', startangle=140, colors=colors)
    plt.title(title)
    plt.axis('equal')  # 保证饼图为圆形
    plt.tight_layout()
    plt.savefig(output_path)
    plt.close()



def style_trigger_analysis(method, min_target_lines, across_project_flag):
    eval_results = EvalResults(method, min_target_lines, across_project_flag)
    # 获取所有success和failure的key
    success_pair_keys = [(r.project_name, r.pair_id) for r in eval_results.get_successes()]
    failure_pair_keys = [(r.project_name, r.pair_id) for r in eval_results.get_failure_modifications()]
    if across_project_flag:
        project_names = ["across-project"]
    else:
        project_names = ProjectConfigs().get_all_project_names()
    pair_dict = create_pair_dict(min_target_lines, project_names)
    success_keys = [(pair_dict[pair_key].src_author, pair_dict[pair_key].target_author, pair_dict[pair_key].src_id) for pair_key in success_pair_keys]
    failure_keys = [(pair_dict[pair_key].src_author, pair_dict[pair_key].target_author, pair_dict[pair_key].src_id) for pair_key in failure_pair_keys]



    running_stat = EgsiRunningStat()
    dir = os.path.join(TMP_DATA, "run-statistic")
    for file in os.listdir(dir):
        running_stat.add_from(os.path.join(dir, file))
            
    print(f"total stats:{len(running_stat.data)}")

    style_stat_of_success = running_stat.cal_all_styles_freq(success_keys)
    style_stat_of_failure = running_stat.cal_all_styles_freq(failure_keys)

    # 构建颜色映射（你可以根据实际样式修改颜色）
    all_styles = set(style_stat_of_success) | set(style_stat_of_failure)
    cmap_name='tab20'
    cmap = matplotlib.colormaps[cmap_name]
    label_colors = {label: cmap(i) for i, label in enumerate(sorted(all_styles))}

    # 调用 draw_pie 函数，传入 label_colors 参数
    draw_pie(
        style_stat_of_success,
        "Triggered Styles of Success Pairs",
        os.path.join(create_eval_results_dir(min_target_lines), "style-stat_of_success.png"),
        label_colors
    )

    draw_pie(
        style_stat_of_failure,
        "Triggered Styles of Failure Pairs",
        os.path.join(create_eval_results_dir(min_target_lines), "style-stat_of_failure.png"),
        label_colors
    )

# Get task which has only success-type or failure-modification-type results
def questionnaire_filter(methods, min_target_codes, project_names, line_range):
    pair_dict = create_pair_dict(min_target_codes, project_names)
    
    
    eval_result_map = {method:EvalResults(method, min_target_codes, "across-project" in project_names) for method in methods}
    
    stats = {}
    running_stat = EgsiRunningStat()
    dir = os.path.join(TMP_DATA, "run-statistic")
    for file in os.listdir(dir):
        running_stat.add_from(os.path.join(dir, file))
        
    codebuff_resulst = ResultManager(create_transformation_result_jsonl_path("codebuff", min_target_codes))
    min_line, max_line = line_range
    
    # 筛选任务：仅包含success和failure-modification类型的结果，并且至少有一个success结果
    pairs = []
    for key in pair_dict:
        project_name, pair_id = key
        
        # codebuff会有无法输出结果的case
        # if codebuff_resulst.get_result(project_name, pair_id) is None or codebuff_resulst.get_result(project_name, pair_id).code:
        #     continue

        src_code = DataCache.get_code_list(project_name, pair_dict[key].src_author, [pair_dict[key].src_id])[0]
        if key not in stats:
            stats[key] = {"success_count":0, "failure_modification_count":0, "src_lines":len(src_code.splitlines())}
        
        for method, eval_results in eval_result_map.items():
            result = eval_results.get_result(project_name, pair_id)
            
            if result and result.transform_type == TranformType.SUCCESS:
                stats[key]["success_count"] += 1
            if result and result.transform_type == TranformType.FAILURE_MODIFICATION:
                stats[key]["failure_modification_count"] += 1
        
        success_count, failure_modification_count = stats[key]["success_count"], stats[key]["failure_modification_count"]
        if stats[key]["src_lines"] > max_line or stats[key]["src_lines"] < min_line  or success_count + failure_modification_count != len(methods):
            continue
        
        if success_count < 1:
            continue
        
        pairs.append(pair_dict[key])
        
    # print(len(pairs))
        
    # 取20%的数据
    # pairs = sorted(pairs, key = lambda pair: running_stat.get_executed_op_types(pair.src_author, pair.target_author, pair.src_id), reverse=True)[0:int(len(pairs) * 0.2)]
    # for p in pairs:
    #     new_pair_dict[(p.project_name, p.pair_id)] = p
    
    style_combinations_map = {}
    # 从每种风格组合（作者组合）中选择一个任务
    for p in pairs:
        style_combinations = (p.src_author,p.target_author )
        if style_combinations not in style_combinations_map:
            style_combinations_map[style_combinations] = []
        style_combinations_map[style_combinations].append(p)

    # 按照各种风格组合包含的pair的比值采样
    total_pairs = sum(len(pairs) for pairs in style_combinations_map.values())
    sample_total = len(pairs)
    new_pair_dict = {}
    random.seed(42)
    for style_combinations, pairs in style_combinations_map.items():
        # 按 pair_id 排序
        pairs = sorted(pairs, key=lambda pair: int(pair.pair_id))

        # 按数量占比计算该 style_combinations 的采样数量
        proportion = len(pairs) / total_pairs
        n_sample = max(1, round(proportion * sample_total))

        # 随机采样 n_sample 个
        sampled_pairs = random.sample(pairs, min(n_sample, len(pairs)))

        for p in sampled_pairs:
            key = (p.project_name, p.pair_id)
            new_pair_dict[key] = p
    
    # 按照egsi执行的转换操作次数排序后，等数量采样
    # for style_combinations, pairs in style_combinations_map.items():
    #     # pair_keys = sorted(pair_keys, key = lambda pair_key: stats[pair_key]["success_count"] * -10000 + stats[pair_key]["src_lines"])
    #     pairs = sorted(pairs, key = lambda pair: running_stat.get_executed_op_types(pair.src_author, pair.target_author, pair.src_id), reverse=True)
    #     p = pairs[0]
    #     key = (p.project_name, p.pair_id)
    #     new_pair_dict[key] = p
        
    # src_set = set()
    # for p in new_pair_dict.values():
    #     src_set.add((p.src_author, p.src_id))
    # print(f"different src files:{len(src_set)}")
    # print(f"style combinations:{len(style_combinations_map)}")
    # print(f"total task: {len(new_pair_dict)}")
    
    return new_pair_dict

def add_forsee_label_for_task(filepath, project_names, methods, min_target_lines):
    with open(filepath, 'r', encoding='utf-8') as f:
        tasks = [json.loads(line) for line in f]
    
    result_map = {}
    
    eval_results_map = {}
    for method in methods:
        eval_results_map[method] = EvalResults(method, min_target_lines, "across-project" in project_names)
        
    pair_dict = create_pair_dict(min_target_lines, project_names)
    pair_dict = {p.src_author+p.target_author+p.src_id : p.pair_id for p in pair_dict.values()}
    for t in tasks:
        t["pair_id"] = pair_dict[t["src_author"]+t["target_author"]+t["src_id"]]
    
    project_name = "across-project"
    for task in tasks:
        pair_id = task["pair_id"]
        forsee_map = {}
        for method, eval_reuslts in eval_results_map.items():
            result = eval_reuslts.get_result(project_name, pair_id)
            forsee_map[method] = result.transform_type
            
        task["forsee_result"] = forsee_map
    
    with open(filepath, "w", encoding="utf-8") as f:
        for task in tasks:
            f.write(json.dumps(task) + "\n")
        

def draw_success_matrix(methods, min_target_lines):
    success_distribution = {}
    all_pairs = set()
    for m in methods:
        results = EvalResults(m, min_target_lines, True)
        success_distribution[m] = set()

        for r in results.get_successes():
            all_pairs.add(r.pair_id)
            success_distribution[m].add(r.pair_id)


    methods = list(success_distribution.keys())
    num_methods = len(methods)


    # 1️⃣ 计算每个交集及具体 pair_id
    intersection_contents = {}
    intersection_sizes = []
    intersection_labels = []

    for combination in product([True, False], repeat=num_methods):
        selected_methods = [methods[i] for i, flag in enumerate(combination) if flag]
        if not selected_methods:
            continue
        intersect = set.intersection(*(success_distribution[m] for m in selected_methods))
        not_selected_methods = [methods[i] for i, flag in enumerate(combination) if not flag]
        if not_selected_methods:
            intersect = intersect - set.union(*(success_distribution[m] for m in not_selected_methods))
        if intersect:
            intersection_contents[combination] = intersect
            intersection_labels.append(combination)
            intersection_sizes.append(len(intersect))

    # 2️⃣ 按黑点数量排序
    sorted_indices = sorted(range(len(intersection_labels)), 
                            key=lambda i: sum(intersection_labels[i]), reverse=True)
    intersection_labels = [intersection_labels[i] for i in sorted_indices]
    intersection_sizes = [intersection_sizes[i] for i in sorted_indices]

    # 3️⃣ 绘制图表
    fig = go.Figure()

    # 3a. 柱状图，顶部显示集合大小，鼠标 hover 显示具体 pair_id
    fig.add_trace(go.Bar(
        x=list(range(len(intersection_labels))),
        y=intersection_sizes,
        text=[str(size) for size in intersection_sizes],  # 柱顶显示集合大小
        textposition='outside',
        hovertext=[', '.join(intersection_contents[comb]) for comb in intersection_labels],
        hoverinfo='text+y',
        marker_color='steelblue',
        name='Intersection Size'
    ))

    # 3b. 点阵图
    for i, method in enumerate(methods):
        y_pos = [-i-1]*len(intersection_labels)
        colors = ['black' if comb[i] else 'lightgray' for comb in intersection_labels]
        fig.add_trace(go.Scatter(
            x=list(range(len(intersection_labels))),
            y=y_pos,
            mode='markers',
            marker=dict(color=colors, size=12),
            showlegend=False,
            hoverinfo='skip'
        ))

    # 3c. 左侧方法名 + 总数
    total_sizes = [len(success_distribution[m]) for m in methods]
    for i, (method, total) in enumerate(zip(methods, total_sizes)):
        fig.add_trace(go.Scatter(
            x=[-2],
            y=[-i-1],
            text=[f"{method} ({total})"],
            mode='text',
            showlegend=False,
            hoverinfo='skip'
        ))

    # 4️⃣ 布局调整
    fig.update_layout(
        title="Success Pairs Intersections",
        xaxis=dict(
            title='Method Set',
            tickvals=[],
            showgrid=False
        ),
        yaxis=dict(
            title='Pair Set',
            tickvals=[],
            showgrid=False,
            autorange=True
        ),
        height=None,
        width=None,
        margin=dict(l=150, r=50, t=80, b=150),
        autosize=True
    )

    fig.write_html("success_diff.html", full_html=True)
    
    print(f"total pairs: {len(all_pairs)}")
    print("Intersection ID -> pair_id list")
    for idx, pairs in intersection_contents.items():
        print(f"{idx}: {pairs}")

def save_code_by_pair(methods, min_target_lines, pair_dict, output_dir):

    if os.path.exists(output_dir):
        shutil.rmtree(output_dir)
        
    # key:method, value:{"success":0, "failure":"", "lines_median":[]}
    stats = {} 
    for method in methods:
        stats[method] = {"lines_median":[]}
        
    eval_results_map = {}
    transform_results_map = {}
    for m in methods:
        eval_results_map[m] = EvalResults(m, min_target_lines, True)
        transform_results_map[m] = ResultManager(create_transformation_result_jsonl_path(m, min_target_lines))
    
    target_map = {
        "A1": ["M873","M925", "M1346"],
        "A133": ["M186","M1359"],
        "A138": ["M504","M536", "M852"],
        "A233": ["M516","M623", "M543"],
        "A248": ["M359","M367"],
        "A305": ["M383","M328", "M372"],
        "A328": ["M817","M1066", "M1117"],
        "A461": ["M1384","M1619"],
        "A591": ["M149","M163", "M185"],
    }


    for p in pair_dict.values():
        project_name, pair_id, src_author, target_author,  src_id = p.project_name, p.pair_id, p.src_author, p.target_author, p.src_id
        dir = os.path.join(output_dir, pair_id)
        os.makedirs(dir, exist_ok=True)
        
        # write Target.java
        src_ids = target_map[target_author]
        target_path = os.path.join(dir, "Target.java")
        if not os.path.exists(target_path):
            for id in src_ids:
                code_list = DataCache.get_code_list(project_name, target_author, [id])
                with open(target_path, 'a') as f:
                    f.write(code_list[0])
                    f.write("\n\n")
                    
        # write src code
        original_src_code = DataCache.get_code_list(project_name, src_author, [src_id])[0]
        src_filename = "original_src"
        with open(os.path.join(dir, f"{src_filename}.java"), 'w') as f:
            f.write(original_src_code)
        
        # write transformation results
        for method, transform_results in transform_results_map.items():
            code = transform_results.get_result(project_name, pair_id).code
            if not code:
                code = original_src_code  
            with open(os.path.join(dir, f"{method}.java"), 'w') as f:
                f.write(code)


def questionnaire_sample(methods, project_names, min_target_lines, n, output_dir):
    success_distribution = {}
    for m in methods:
        results = EvalResults(m, min_target_lines, True)
        success_distribution[m] = set()

        for r in results.get_successes():
            success_distribution[m].add(r.pair_id)

    # 1️⃣ 计算每个交集及具体 pair_id
    intersection_contents = {}
    for combination in product([True, False], repeat=len(methods)):
        selected_methods = [methods[i] for i, flag in enumerate(combination) if flag]
        if not selected_methods:
            continue
        intersect = set.intersection(*(success_distribution[m] for m in selected_methods))
        not_selected_methods = [methods[i] for i, flag in enumerate(combination) if not flag]
        if not_selected_methods:
            intersect = intersect - set.union(*(success_distribution[m] for m in not_selected_methods))
        if intersect:
            intersection_contents[combination] = intersect
            
    # 从每个交集中随机抽取n个，保存代码
    random.seed(42)
    pair_ids = []
    for ids in intersection_contents.values():
        pair_ids.extend(random.sample(sorted(ids), min(len(ids), n)))
    pair_dict = create_pair_dict(min_target_lines, project_names)
    pair_dict = {key : p for key, p in pair_dict.items() if p.pair_id in pair_ids}
    save_code_by_pair(methods,min_target_lines, pair_dict, output_dir)
    
    # 输出每个method的success和failure的pair的比例
    selected_pairs = set(pair_ids)
    all_pairs = set()
    for pairs in success_distribution.values():
        all_pairs = all_pairs.union(pairs)
    pairs = all_pairs
    print("Method success/failure ratio (selected pairs only):")
    for m in methods:
        success_count = len(success_distribution[m] & pairs)
        failure_count = len(pairs - success_distribution[m])
        total = success_count + failure_count
        print(f"{m}: {success_count}/{total} ({success_count/total:.2%} success)")
    



def print_statistic(methods, pair_dict, min_target_code_lines):
    # 总的任务数量
    # with open(tasks_file, 'r', encoding='utf-8') as f:
    #     tasks = [json.loads(line) for line in f]
    # task_keys = [t['target_author']+t['src_author']+t['src_id'] for t in tasks]
    print(f"Total tasks: {len(pair_dict)}")
        
    # 计算风格组合数量
    style_combinations = set((p.target_author, p.src_author) for p in pair_dict.values())
    print(f"style combinations: {len(style_combinations)}")
    
    print("Success rate of each method:")
    target_pair_ids = [p.pair_id for p in pair_dict.values()]
    for m in methods:
        eval_results = EvalResults(m, min_target_code_lines, True)
        successes_in_target = [r for r in eval_results.get_successes() if r.pair_id in target_pair_ids]
        success_rate = round(len(successes_in_target)/len(pair_dict) * 100, 2)
        print(f"{m}: {success_rate}")

def update_target_ids_of_tasks(tasks_file, pair_dict):
    with open(tasks_file, 'r', encoding='utf-8') as f:
        tasks = [json.loads(line) for line in f]
    target_ids_map  = {p.target_author+p.src_author+p.src_id:p.target_ids for p in pair_dict.values()}
    for t in tasks:
        t['target_ids'] = target_ids_map[t['target_author']+t['src_author']+t['src_id']]
        
    with open(tasks_file, 'w', encoding='utf-8') as f:
        for t in tasks:
            json.dump(t, f)
            f.write('\n')


def questionnaire_filter_by_file(tasks_file, pair_dict):
    with open(tasks_file, 'r', encoding='utf-8') as f:
        tasks = [json.loads(line) for line in f]
    task_keys = [t['target_author']+t['src_author']+t['src_id'] for t in tasks]
    target_pairs = {key:pair_dict[key] for key in pair_dict if pair_dict[key].target_author+pair_dict[key].src_author+pair_dict[key].src_id in  task_keys }
    return target_pairs

def filter_more_than_format(methods, min_target_codes, pair_dict, line_range):
    result_map = {method:ResultManager(create_transformation_result_jsonl_path(method, min_target_codes)) for method in methods}
    result = {}
    for key in pair_dict.keys():
        p = pair_dict[key]
        original_src = DataCache.get_code_list(p.project_name, p.src_author, [p.src_id])[0]
        lines = len(original_src.splitlines())
        if lines < line_range[0] or lines > line_range[1]:
            continue
        
        modify_more_than_format = 0
        for r in result_map.values():
            r = r.get_result(p.project_name, p.pair_id)
            if r and re.sub('\s+', '', r.code) != re.sub('\s+', '', original_src):
                modify_more_than_format += 1
            
        if modify_more_than_format > 0:
            result[key] = p
                
    return result

def sample_dict(d: dict, n_percent: float) -> dict:
    random.seed(42)
    
    # 计算需要抽取的数量
    sample_size = max(1, int(len(d) * n_percent))
    
    # 随机抽取键
    keys = random.sample(sorted(list(d.keys())), sample_size)
    
    # 构造子字典
    return {k: d[k] for k in keys}


    
def get_failed_pairs(pair_dict, min_target_lines, method="egsi"):
    file = create_transformation_result_jsonl_path(method, min_target_lines)
    manager = ResultManager(file)
    failed = []
    for r in manager.get_all_results():
        if r.test_passed != "" and not r.test_passed:
            failed.append(r.pair_id)
    return {k:v for k, v in pair_dict.items() if v.pair_id in failed}


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
            # "claude-3.7-sonnet"
        ]

min_target_codes = 200
project_names = ["across-project"]
output_dir = os.path.join(TMP_DATA, "code-materials")


# add_forsee_label_for_task("tasks.jsonl", project_names, methods, min_target_codes)
# exit(0)

pair_dict = create_pair_dict(min_target_codes, project_names)
task_dict = questionnaire_filter_by_file(os.path.join(HUMAN_STUDY_DIR, "tasks.jsonl"), pair_dict)
# pair_dict = questionnaire_filter(methods, min_target_codes, project_names, [1,100])
# pair_dict = filter_more_than_format(methods, min_target_codes, pair_dict, [0,100])

# srcs = set()
# for t in task_dict.values():
#     srcs.add(t.src_author + t.src_id)
# pair_dict = {k:v for k, v in pair_dict.items() if k not in task_dict and v.src_author + v.src_id not in srcs}

# pair_dict = get_failed_pairs(pair_dict, min_target_codes, "codebuff")

pair_dict = task_dict
print_statistic(methods, pair_dict, min_target_codes)



# style_trigger_analysis("egsi", min_target_codes, False)
save_code(methods, min_target_codes, project_names, output_dir, pair_dict, lambda x:True)


# draw_success_matrix(methods, min_target_codes)
# questionnaire_sample(methods,project_names,  min_target_codes, 2, "../tmp-data/code-materials")

# print_statistic(methods, "tasks.jsonl", pair_dict, min_target_codes)