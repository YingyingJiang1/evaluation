import os
import csv

import numpy as np
import os
import re
import json
import pandas as pd
import matplotlib.pyplot as plt
from collections import defaultdict, Counter
from dataclasses import dataclass
from statistics import median

HUMAN_STUDY_DIR = "../evaluation/human_study"
name_map_file = os.path.join(HUMAN_STUDY_DIR, "name_map.json")
tasks_jsonl_file = os.path.join(HUMAN_STUDY_DIR,"tasks.jsonl" )


@dataclass
class Item:
    task_id: str
    programming_experience: str
    konw_java: bool
    styles_cared_about:list[str]
    success_map: dict[str, bool]
    format_score_map: dict[str, float]
    syntax_score_map: dict[str, float]
    semantic_score_map: dict[str, float]
    test_question_results:list[str]
    tele:str
    time:str
    forsee_result:dict[str, str]


def parse_score(value: str) -> float:
    """把问卷里的 `D.4（相似）` 解析成分数 float"""
    if not value or "." not in value:
        return 0.0
    try:
        # 取出形如 "D.4（相似）" 的数字部分
        return float(re.search(r"\d+", value).group())
    except Exception:
        return 0.0
def save_as_csv(results, output_file):
    # 定义 CSV 列名
    fieldnames = [
        "task_id",
        "programming_experience",
        "konw_java",
        "styles_cared_about",
        "success_map",
        "format_score_map",
        "syntax_score_map",
        "semantic_score_map",
        "test_question_results",
        "tele",
        "time",
        "forsee_result"
    ]

    with open(output_file, "w", encoding="utf-8-sig", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        for r in results:
            writer.writerow({
                "task_id": r.task_id,
                "programming_experience": r.programming_experience,
                "konw_java": r.konw_java,
                "styles_cared_about": json.dumps(r.styles_cared_about, ensure_ascii=False),
                "success_map": json.dumps(r.success_map, ensure_ascii=False),
                "format_score_map": json.dumps(r.format_score_map, ensure_ascii=False),
                "syntax_score_map": json.dumps(r.syntax_score_map, ensure_ascii=False),
                "semantic_score_map": json.dumps(r.semantic_score_map, ensure_ascii=False),
                "test_question_results": json.dumps(r.test_question_results, ensure_ascii=False),
                "tele": r.tele,
                "time": r.time,
                "forsee_result":json.dumps(r.forsee_result, ensure_ascii=False)
            })
    
def preprocess(input_dir, output_file):
    # 加载 name_map
    with open(name_map_file, "r", encoding="utf-8") as f:
        name_map = json.load(f)
        
    # 加载tasks.jsonl
    with open(tasks_jsonl_file, "r", encoding="utf-8") as f:
        tasks = [json.loads(line) for line in f]
    task_dict = {task["task_id"]: task for task in tasks}


    # 匹配任务链接中的 task_id
    task_pattern = re.compile(r"task=(task\d+)&view=(\d-?\d?)")

    # 最终结果
    results:list[Item] = []
    cur_task_id = -1

    # 遍历所有答卷文件
    for file in os.listdir(input_dir):
        if not file.endswith(".csv"):
            continue
        file_path = os.path.join(input_dir, file)
        with open(file_path, "r", encoding="utf-8-sig") as f:
            reader = csv.DictReader(f)
            for row in reader:
                # 先把 row 中所有值转成字符串
                for k, v in row.items():
                    if isinstance(v, list):
                        # 把列表转成单个字符串
                        row[k] = ",".join(v)
                    elif v is None:
                        row[k] = ""
                
                task_id, view_id = -1, -1
                tele = row.get("1.请输入您的支付宝账号（此账号仅用于支付报酬，不会泄露）", "").strip()
                programming_experience = row.get("2.您的编程经验", "").split(".")[1]
                konw_java = row.get("3.您之前是否接触或使用过Java", "").split(".")[1]
                time = f'{int(row.get("答题时长", "")) / 60.0:.1f}min'

                # styles cared about
                styles_cared_about = []
                # 测试答卷有效性的题目
                test_question_results = []
                # 每个 task 的数据
                task_data = {}  # task_id -> {success_map, format_score_map, syntax_score_map, semantic_score_map}
                for key, value in row.items():
                    if not value or not key:
                        continue
                    
                    if key and key.startswith("4.您比较关注哪方面的代码风格？"):
                        if len(row[key].split(".")) > 1:
                            value = row[key].split(".")[1]
                            if "其他" in value:
                                value = "其他"
                            styles_cared_about.append(value)
                        
                    elif "此题请选择" in key:
                        test_question_results.append(row[key].split(".")[1])
                    else:
                        m = task_pattern.search(key)
                        if m:
                            task_id = m.group(1)
                            view_id = m.group(2)
                        if task_id != -1:
                            if task_id not in task_data:
                                task_data[task_id] = {
                                    "success_map": {},
                                    "format_score_map": {},
                                    "syntax_score_map": {},
                                    "semantic_score_map": {},
                                    "test_question_results": test_question_results,  # 可以复用整个问卷的选择题结果
                                }

                            # 单选题：选择最相似的选项
                            if "最相似的选项" in key:
                                question_name_map = name_map[task_id][view_id]
                                option = value.split(".")[1]
                                file = question_name_map["A"] if not question_name_map["A"].startswith("original_src") else question_name_map["B"]
                                task_data[task_id]["success_map"][file.replace(".java", "")] = question_name_map[option] == file
                            elif "打分" in key:
                                question_name_map = name_map[task_id]["2"]
                                option = key.split(":")[-1]  # A/B/C...
                                current_method_name = question_name_map[option].replace(".java", "")
                                # 处理打分 (格式/语法/语义)
                                if "格式风格相似度" in key:
                                    task_data[task_id]["format_score_map"][current_method_name] = parse_score(value)
                                elif "语法风格相似度" in key:
                                    task_data[task_id]["syntax_score_map"][current_method_name] = parse_score(value)
                                elif "语义风格相似度" in key:
                                    task_data[task_id]["semantic_score_map"][current_method_name] = parse_score(value)
                # 无效答卷
                if test_question_results != ["C", "C"]:
                    continue
                
                # 对每个 task 生成一个 Item
                for task_id, data in task_data.items():
                    results.append(
                        Item(
                            task_id=task_id,
                            programming_experience=programming_experience,
                            konw_java=konw_java,
                            styles_cared_about=styles_cared_about,
                            success_map=data["success_map"],
                            format_score_map=data["format_score_map"],
                            syntax_score_map=data["syntax_score_map"],
                            semantic_score_map=data["semantic_score_map"],
                            test_question_results=data["test_question_results"],
                            tele=tele,
                            time=time,
                            forsee_result=task_dict[task_id]["forsee_result"]
                        )
                    )

    results.sort(key=lambda r: int(r.task_id[4:]))
    # 保存结果
    save_as_csv(results, output_file)

def process(input_csv: str, output_csv: str):
    # 读取 CSV 并解析 JSON 字段
    items_by_task = defaultdict(list)
    with open(input_csv, "r", encoding="utf-8-sig") as f:
        reader = csv.DictReader(f)
        for row in reader:
            task_id = row["task_id"]
            success_map = json.loads(row.get("success_map", "{}"))
            format_score_map = json.loads(row.get("format_score_map", "{}"))
            syntax_score_map = json.loads(row.get("syntax_score_map", "{}"))
            semantic_score_map = json.loads(row.get("semantic_score_map", "{}"))
            items_by_task[task_id].append({
                "success_map": success_map,
                "format_score_map": format_score_map,
                "syntax_score_map": syntax_score_map,
                "semantic_score_map": semantic_score_map
            })
            
    # 加载tasks.jsonl
    with open(tasks_jsonl_file, "r", encoding="utf-8") as f:
        tasks = [json.loads(line) for line in f]
    task_dict = {task["task_id"]: task for task in tasks}

    # 聚合每个 task_id 的数据
    merged_items = []
    for task_id, group in items_by_task.items():
        # 去除只有一个答卷的 task
        if len(group) == 1:
            continue
        
        # success_map：同一个 key 取频率最大的值
        all_keys = set(k for item in group for k in item["success_map"].keys())
        combined_success = {}
        for k in all_keys:
            values = [item["success_map"].get(k) for item in group if k in item["success_map"]]
            count = Counter(values).most_common(1)[0][1]
            if len(group) > 1 and count > len(group) / 2:
                combined_success[k] = Counter(values).most_common(1)[0][0]

        # score_map：同一个 key 取中位数
        def average_score(map_name):
            combined = {}
            all_keys = set(k for item in group for k in item[map_name].keys())
            for k in all_keys:
                scores = [item[map_name].get(k, 0) for item in group if k in item[map_name]]
                # combined[k] = round(sum(scores) / len(scores), 2)
                combined[k] = round(median(scores), 2)
            return combined


        merged_items.append({
            "task_id": task_id,
            "success_map": combined_success,
            "format_score_map": average_score("format_score_map"),
            "syntax_score_map": average_score("syntax_score_map"),
            "semantic_score_map": average_score("semantic_score_map"),
            "forsee_result":task_dict[task_id]["forsee_result"]
        })

    # 按 task_id 排序
    merged_items.sort(key=lambda x: int(x["task_id"][4:]))

    # 保存为 CSV
    with open(output_csv, "w", encoding="utf-8-sig", newline="") as f:
        fieldnames = ["task_id", "success_map", "format_score_map", "syntax_score_map", "semantic_score_map", "forsee_result"]
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        for item in merged_items:
            writer.writerow({
                "task_id": item["task_id"],
                "success_map": json.dumps(item["success_map"], ensure_ascii=False),
                "format_score_map": json.dumps(item["format_score_map"], ensure_ascii=False),
                "syntax_score_map": json.dumps(item["syntax_score_map"], ensure_ascii=False),
                "semantic_score_map": json.dumps(item["semantic_score_map"], ensure_ascii=False),
                "forsee_result":json.dumps(item["forsee_result"], ensure_ascii=False)
            })
            

def basic_info_graph(input_csv: str, output_dir):
    plt.rcParams['font.sans-serif'] = ['SimHei']
    plt.rcParams['axes.unicode_minus'] = False

    os.makedirs(output_dir, exist_ok=True)

    programming_exp_counter = Counter()
    know_java_counter = Counter()
    styles_counter = Counter()

    with open(input_csv, "r", encoding="utf-8-sig") as f:
        reader = csv.DictReader(f)
        for row in reader:
            # 编程经验
            programming_exp = row.get("programming_experience", "").strip()
            if programming_exp:
                programming_exp_counter[programming_exp] += 1

            # 是否接触过 Java
            know_java = row.get("konw_java", "").strip()
            if know_java:
                know_java_counter[know_java] += 1

            # 关注的风格（去重）
            styles = row.get("styles_cared_about", "").strip()
            if styles:
                try:
                    styles_list = set(json.loads(styles))  # 去重
                    for s in styles_list:
                        styles_counter[s] += 1
                except:
                    pass

    # 绘制饼图
    def plot_pie(counter, title, filename):
        labels = list(counter.keys())
        sizes = list(counter.values())
        plt.figure(figsize=(6,6))
        plt.pie(sizes, labels=labels, autopct='%1.1f%%', startangle=140)
        plt.title(title)
        plt.axis('equal')
        plt.tight_layout()
        plt.savefig(os.path.join(output_dir, filename), format='png')
        plt.close()

    # 绘制条形图
    def plot_bar(counter, title, filename):
        labels = list(counter.keys())
        values = list(counter.values())
        plt.figure(figsize=(8,6))
        plt.barh(labels, values, color='skyblue')
        plt.xlabel("人数")
        plt.title(title)
        plt.tight_layout()
        plt.savefig(os.path.join(output_dir, filename), format='png')
        plt.close()

    # 编程经验和是否接触过 Java 用饼图
    plot_pie(programming_exp_counter, "编程经验分布", "programming_experience.png")
    plot_pie(know_java_counter, "是否接触过 Java", "know_java.png")
    # 关注的风格用条形图
    plot_bar(styles_counter, "关注的代码风格分布（人数）", "styles_cared_about.png")


def cal_success_rate(input_csv: str, output_csv: str):
    df = pd.read_csv(input_csv)

    # 统计结果
    stats = defaultdict(lambda: {
        "true_total": [0, 0],      # [成功次数, 总次数]
        "forsee_success": [0, 0],  # [成功次数, 总次数]
        "forsee_failure": [0, 0]   # [成功次数, 总次数]
    })

    for _, row in df.iterrows():
        success_map = json.loads(row["success_map"])
        forsee_result = json.loads(row["forsee_result"])

        for method, result in forsee_result.items():
            if method not in success_map:
                continue

            is_success = success_map[method]

            # 总体
            stats[method]["true_total"][1] += 1
            if is_success:
                stats[method]["true_total"][0] += 1

            # 分类统计
            if result == "Success":
                stats[method]["forsee_success"][1] += 1
                if is_success:
                    stats[method]["forsee_success"][0] += 1
            else:  # Failure-*
                stats[method]["forsee_failure"][1] += 1
                if is_success:
                    stats[method]["forsee_failure"][0] += 1

    # 整理结果
    records = []
    for method, values in stats.items():
        true_s, true_t = values["true_total"]
        fs_s, fs_t = values["forsee_success"]
        ff_s, ff_t = values["forsee_failure"]

        records.append({
            "Method": method,
            "Success Rate(%)": round(true_s / true_t * 100, 2) if true_t > 0 else None,
            "Forsee-Success Rate(%)": round(fs_s / fs_t * 100, 2) if fs_t > 0 else None,
            "Forsee-Failure Rate(%)": round(ff_s / ff_t * 100, 2) if ff_t > 0 else None
        })

    result_df = pd.DataFrame(records)
    result_df.to_csv(output_csv, index=False, encoding="utf-8-sig")
    return records


import pandas as pd
import numpy as np
import json
import matplotlib.pyplot as plt

def plot_score_radar(input_csv: str, output_file: str):
    """
    绘制方法在 Format、Syntax、Semantic 三个维度的雷达图
    原始代码 original_src 作为基线
    """
    df = pd.read_csv(input_csv)
    categories = ["Format", "Syntax", "Semantic"]

    # 方法 -> {维度: 分数列表}
    method_scores = {}
    baseline_scores = {dim: [] for dim in categories}

    for _, row in df.iterrows():
        format_scores = json.loads(row["format_score_map"])
        syntax_scores = json.loads(row["syntax_score_map"])
        semantic_scores = json.loads(row["semantic_score_map"])

        # original_src 作为基线
        for dim, scores_map in zip(categories, [format_scores, syntax_scores, semantic_scores]):
            baseline_scores[dim].append(scores_map.get("original_src", 0))

        # 各方法得分
        for method in format_scores.keys():
            if method == "original_src":
                continue
            if method not in method_scores:
                method_scores[method] = {dim: [] for dim in categories}
            for dim, scores_map in zip(categories, [format_scores, syntax_scores, semantic_scores]):
                method_scores[method][dim].append(scores_map[method])

    # 计算每个方法每个维度的平均得分
    method_avg = {
        method: {dim: np.mean(vals) for dim, vals in scores.items()}
        for method, scores in method_scores.items()
    }

    # 原始代码基线平均得分
    baseline_avg = [np.mean(baseline_scores[dim]) for dim in categories]

    # 雷达图角度
    N = len(categories)
    angles = np.linspace(0, 2 * np.pi, N, endpoint=False).tolist()
    angles += angles[:1]

    plt.figure(figsize=(8, 8))
    ax = plt.subplot(111, polar=True)
    linewidth = 1.5


    # 绘制方法
    for method, scores in method_avg.items():
        values = [scores[dim] for dim in categories]
        values += values[:1]
        ax.plot(angles, values, label=method, linewidth=linewidth)
        ax.fill(angles, values, alpha=0.1)

    # 绘制 original_src 基线
    baseline_values = baseline_avg + baseline_avg[:1]
    ax.plot(angles, baseline_values, '--', color='black', linewidth=linewidth, label='Original Code')

    ax.set_xticks(angles[:-1])
    ax.set_xticklabels(categories, fontsize=12)
    ax.set_title("Comparison of Code Style Transformation Methods Across Style Dimensions", fontsize=14)
    ax.legend(loc="lower right", bbox_to_anchor=(1.2, 0))  # legend 放在图右下方外部

    
    ax.set_ylim(2, 4)

    plt.savefig(output_file, format="png")
    plt.close()


def plot_method_boxplot(input_csv: str, output_dir: str):
    df = pd.read_csv(input_csv)

    # 定义维度
    categories = ["format_score_map", "syntax_score_map", "semantic_score_map"]
    dim_names = ["Format", "Syntax", "Semantic"]


    # 收集每个方法在每个维度上的分数列表
    all_data = {dim: {} for dim in dim_names}

    for _, row in df.iterrows():
        for cat, dim in zip(categories, dim_names):
            scores = json.loads(row[cat])
            for method, score in scores.items():
                if method not in all_data[dim]:
                    all_data[dim][method] = []
                all_data[dim][method].append(score)

    # 分别绘制每个维度的箱线图
    for dim in dim_names:
        methods = list(all_data[dim].keys())
        data = [all_data[dim][m] for m in methods]

        plt.figure(figsize=(8, 6))
        plt.boxplot(data, labels=methods)
        plt.title(f"{dim} Style Similarity Score Distribution", fontsize=14)
        plt.ylabel("Style Similarity Score")
        plt.grid(True, linestyle='--', alpha=0.5)

        # 保存为 PNG 矢量图可选 png
        output_file = os.path.join(output_dir, f"{dim}_style_boxplot.png")
        plt.tight_layout()
        plt.savefig(output_file, format="png")
        plt.close()
        
def plot_success_rate(records, output_file: str):
    df = pd.DataFrame(records)

    methods = df["Method"].tolist()
    x = range(len(methods))

    plt.figure(figsize=(8, 6))

    # 绘制三条折线
    plt.plot(x, df["Success Rate(%)"], marker='o', label="Overall")
    plt.plot(x, df["Forsee-Success Rate(%)"], marker='s', label="Forsee Success")
    plt.plot(x, df["Forsee-Failure Rate(%)"], marker='^', label="Forsee Failure")

    plt.xticks(x, methods, rotation=45)
    plt.ylabel("Success Rate (%)")
    plt.xlabel("Method")
    plt.title("Comparison of Success Rates Across Methods", fontsize=14)
    plt.ylim(0, 100)  # 百分比显示
    plt.grid(True, linestyle='--', alpha=0.5)
    plt.legend(loc="lower right")

    plt.tight_layout()
    plt.savefig(output_file, format="png")
    plt.close()

def plot_overall_success_rate(output_file: str):
    methods = ["egsi", "codebuff", "deepseek-r1-0528--free", "gpt-4.1"]

    # Forsee 结果
    forsee_rates = [43.33, 45.0, 30.0, 48.33]

    # Human Study 结果
    human_rates = [57.14, 43.64, 51.92, 58.49]

    plt.figure(figsize=(8, 5))
    
    # 绘制折线
    plt.plot(methods, human_rates, label="Human Study", marker="o", linewidth=2, markersize=6, color="#1f77b4")
    plt.plot(methods, forsee_rates, label="Forsee", marker="s", linewidth=2, markersize=6, color="#ff7f0e")
    
    plt.xlabel("Method", fontsize=12)
    plt.ylabel("Success Rate (%)", fontsize=12)
    plt.title("Overall Success Rate of Code Transformation Methods", fontsize=14)
    plt.ylim(0, 100)
    plt.grid(True, linestyle="--", alpha=0.5)
    plt.legend(loc="lower right", fontsize=10)
    
    plt.tight_layout()
    plt.savefig(output_file, format="png")
    plt.close()
    

if __name__ == "__main__":
    results_dir = os.path.join(HUMAN_STUDY_DIR, "results")
    raw_data_path = os.path.join(results_dir, "processed_data.csv")
    data_path = os.path.join(results_dir, "data.csv")
    questionnar_data_dir = os.path.join(HUMAN_STUDY_DIR, "data")
    
    preprocess(questionnar_data_dir, raw_data_path)
    process(raw_data_path, data_path)
    
    graphs_dir = os.path.join(results_dir, "graphs")
    basic_info_graph(raw_data_path, graphs_dir)
    success_rate_data = cal_success_rate(data_path, os.path.join(results_dir, "success_rates.csv"))
    plot_score_radar(data_path, os.path.join(graphs_dir, "score_radar.png"))
    plot_method_boxplot(data_path, graphs_dir)
    
    plot_success_rate(success_rate_data, os.path.join(graphs_dir, "success_rate.png"))
    plot_overall_success_rate(os.path.join(graphs_dir, "overall_success_rate.png"))
    
    methods = [
            "egsi",
            "codebuff",
            "deepseek-r1-0528--free",
            "gpt-4.1",
            # "claude-3.7-sonnet"
        ]
    # for method in methods:
    #     success_rate_data.append({
    #         "Method": method,
    #         "Success Rate(%)": round(true_s / true_t * 100, 2) if true_t > 0 else None,
    #         "Forsee-Success Rate(%)": None,
    #         "Forsee-Failure Rate(%)": None
    #     })
     
    
    # with open("name_map.json", "r", encoding="utf-8") as f:
    #     name_map = json.load(f)
    #     cal_success_rate_across_forsee_result(raw_data_path, "./results/success_rates_by_forsee_result.csv")
    #     cal_method_success_rates(name_map, raw_data_path, "./results/method_success_rates.csv", methods)
    # similarity_distribution_boxplot(data_path, "./results")
    
    # methods = ["gpt-4.1", "deepseek-r1-0528--free", "egsi", "claude-3.7-sonnet", "codebuff"]
    # similarity_score_bar_by_forsee_result(data_path, "./results")
    # # similarity_score_boxplot_by_forsee_result(data_path, "./results")
    # classify_and_calc_distribution("./results/processed_data.csv", "egsi", 4, "./results/")
    