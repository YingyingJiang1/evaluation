import os
import csv

import numpy as np
import os
import re
import json
import pandas as pd
import matplotlib.pyplot as plt
from collections import defaultdict, Counter
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score
from dataclasses import dataclass
from statistics import median
import matplotlib.pyplot as plt


HUMAN_STUDY_DIR = "./"

name_map_file = os.path.join(HUMAN_STUDY_DIR, "name_map.json")
tasks_jsonl_file = os.path.join(HUMAN_STUDY_DIR,"tasks.jsonl" )


label_map = {
        "egsi":"SmartStyler",
        "codebuff":"CodeBuff",
        "deepseek-r1-0528--free":"DeepSeek-R1",
        "gpt-4.1":"GPT-4.1"

    }
axis_font_size = 10
font_size = 10

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
    # tele:str
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
        # "tele",
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
                # "tele": r.tele,
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

    times = []
    teles = []

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
                
                float_time =  round(int(row.get("答题时长", "")) / 60.0, 1)
                times.append(float_time)
                teles.append(tele)
                if float_time < 5:
                    print(float_time)
                    continue

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
                            # tele=tele,
                            time=time,
                            forsee_result=task_dict[task_id]["forsee_result"]
                        )
                    )

    results.sort(key=lambda r: int(r.task_id[4:]))
    # 保存结果
    save_as_csv(results, output_file)
    
    
    times = np.array(times)
    threash = 30
    times1 = [t for t in times if t <= threash]
    times2 = [t for t in times if t > threash]
    print(f"<={threash}: {len(times1)}, >{threash}: {len(times2)}")
    print(f"min:{min(times)}, max:{max(times)}, mean:{np.median(times)}")
    print(times)
    with open(os.path.join(os.path.dirname(output_file), "tele.txt"), "w", encoding="utf-8") as f:
        f.write("\n".join(teles))


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
            if len(group) > 1 and count > len(group) / 2.0:
                combined_success[k] = Counter(values).most_common(1)[0][0]

        # score_map：同一个 key 取中位数
        def median_score(map_name):
            combined = {}
            all_keys = set(k for item in group for k in item[map_name].keys())
            for k in all_keys:
                scores = [item[map_name].get(k, 0) for item in group if k in item[map_name]]
                # combined[k] = round(sum(scores) / len(scores), 2)
                combined[k] = round(median(scores), 2)
            return combined

        if len(combined_success) == len(all_keys):
            
            merged_items.append({
                "task_id": task_id,
                "success_map": combined_success,
                "format_score_map": median_score("format_score_map"),
                "syntax_score_map": median_score("syntax_score_map"),
                "semantic_score_map": median_score("semantic_score_map"),
                "forsee_result":task_dict[task_id]["forsee_result"]
            })
        else:
            print(combined_success)

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
            
import csv
import json
import os
from collections import Counter
import pandas as pd

import csv
import json
import os
import pandas as pd
from collections import Counter

def basic_info_to_csv(input_csv: str, output_csv: str):
    """
    读取用户信息数据，统计三项内容并输出到一个 CSV 文件：
    - 编程经验 (programming_experience)
    - 是否了解 Java (konw_java)
    - 关注的风格 (styles_cared_about)
    同时输出百分比 (%)
    """
    programming_exp_counter = Counter()
    know_java_counter = Counter()
    styles_counter = Counter()

    # === 1. 读取输入文件 ===
    with open(input_csv, "r", encoding="utf-8-sig") as f:
        reader = csv.DictReader(f)
        for row in reader:
            # 编程经验统计
            exp = row.get("programming_experience", "").strip()
            if exp:
                programming_exp_counter[exp] += 1

            # 是否了解 Java
            know_java = row.get("konw_java", "").strip()
            if know_java:
                know_java_counter[know_java] += 1

            # 关注的风格（去重计数）
            styles = row.get("styles_cared_about", "").strip()
            if styles:
                try:
                    style_list = ast.literal_eval(styles)
                    if isinstance(style_list, list):
                        clean_styles = [s.strip() for s in style_list if isinstance(s, str) and s.strip()]
                        styles_counter.update(clean_styles)
                except (ValueError, SyntaxError):
                    # 如果不是合法列表字符串就跳过
                    continue

    # === 2. 转为 DataFrame 并计算百分比 ===
    def add_percentage(df, value_col="count"):
        total = df[value_col].sum()
        df["percentage"] = (df[value_col] / total * 100).round(2)
        return df

    exp_df = pd.DataFrame(list(programming_exp_counter.items()), columns=["programming_experience", "count"])
    java_df = pd.DataFrame(list(know_java_counter.items()), columns=["konw_java", "count"])
    styles_df = pd.DataFrame(list(styles_counter.items()), columns=["style", "count"])

    exp_df = add_percentage(exp_df)
    java_df = add_percentage(java_df)
    styles_df = add_percentage(styles_df)

    # === 3. 输出到一个 CSV 文件 ===
    os.makedirs(os.path.dirname(output_csv), exist_ok=True)
    with open(output_csv, "w", encoding="utf-8-sig") as f:
        f.write("# ===== Programming Experience =====\n")
        exp_df.to_csv(f, index=False)
        f.write("\n# ===== Know Java =====\n")
        java_df.to_csv(f, index=False)
        f.write("\n# ===== Styles Cared About =====\n")
        styles_df.to_csv(f, index=False)

    print(f"✅ Combined basic info file saved to: {output_csv}")
    return exp_df, java_df, styles_df

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
        plt.savefig(os.path.join(output_dir, filename), format='eps')
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
        plt.savefig(os.path.join(output_dir, filename), format='eps')
        plt.close()

    # 编程经验和是否接触过 Java 用饼图
    plot_pie(programming_exp_counter, "编程经验分布", "programming_experience.pdf")
    plot_pie(know_java_counter, "是否接触过 Java", "know_java.pdf")
    # 关注的风格用条形图
    plot_bar(styles_counter, "关注的代码风格分布（人数）", "styles_cared_about.pdf")


def cal_success_rate(input_csv: str, output_csv: str):
    df = pd.read_csv(input_csv)

    # 统计结果
    stats = defaultdict(lambda: {
        "true_total": [0, 0],      # [成功次数, 总次数]
        "forsee_true_total": [0, 0],  # [成功次数, 总次数]
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
            is_forsee_success = forsee_result[method].lower().startswith("success")

            # 总体
            stats[method]["true_total"][1] += 1
            if is_success:
                stats[method]["true_total"][0] += 1
                
            stats[method]["forsee_true_total"][1] += 1
            if is_forsee_success:
                stats[method]["forsee_true_total"][0] += 1
                
            

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
        forsee_true_s, forsee_true_t = values["forsee_true_total"]

        records.append({
            "Method": method,
            "Success Rate(%)": round(true_s / true_t * 100, 2) if true_t > 0 else None,
            "Forsee-Success Rate(%)": round(forsee_true_s / forsee_true_t * 100, 2) if forsee_true_t > 0 else None,
        })

    result_df = pd.DataFrame(records)
    result_df.to_csv(output_csv, index=False, encoding="utf-8-sig")
    return records


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

    #     # 各方法得分
        for method in format_scores.keys():
            if method == "original_src":
                continue
            if method not in method_scores:
                method_scores[method] = {dim: [] for dim in categories}
            for dim, scores_map in zip(categories, [format_scores, syntax_scores, semantic_scores]):
                method_scores[method][dim].append(scores_map[method])

    # # 计算每个方法每个维度的平均得分
    method_avg = {
        method: {dim: np.mean(vals) for dim, vals in scores.items()}
        for method, scores in method_scores.items()
    }

    # 原始代码基线平均得分
    baseline_avg = [np.mean(baseline_scores[dim]) for dim in categories]
    
    # all_scores = {dim: {} for dim in categories}
    # baseline_scores = {dim: [] for dim in categories}

    # # === 遍历每一行（任务） ===
    # for _, row in df.iterrows():
    #     format_scores = json.loads(row["format_score_map"])
    #     syntax_scores = json.loads(row["syntax_score_map"])
    #     semantic_scores = json.loads(row["semantic_score_map"])

    #     for dim, scores_map in zip(categories, [format_scores, syntax_scores, semantic_scores]):
    #         # 记录 baseline
    #         baseline_scores[dim].append(scores_map.get("original_src", 0))

    #         # 累积所有方法的分数
    #         for method, val in scores_map.items():
    #             if method == "original_src":
    #                 continue
    #             all_scores[dim].setdefault(method, []).append(val)

    # # === 计算每个方法在每个维度的平均分 ===
    # method_avg = {}
    # for dim in categories:
    #     for method, vals in all_scores[dim].items():
    #         if method not in method_avg:
    #             method_avg[method] = {}
    #         method_avg[method][dim] = np.mean(vals)
    # baseline_avg = [np.mean(baseline_scores[dim]) for dim in categories]
    

    # 雷达图角度
    N = len(categories)
    angles = np.linspace(0, 2 * np.pi, N, endpoint=False).tolist()
    angles += angles[:1]

    plt.figure(figsize=(4, 3))
    ax = plt.subplot(111, polar=True)
    linewidth = 1.5

    # 绘制方法
    for method, scores in method_avg.items():
        values = [scores[dim] for dim in categories]
        values += values[:1]
        ax.plot(angles, values, label=label_map[method], linewidth=linewidth)
        ax.fill(angles, values, alpha=0.1)

    # 绘制 original_src 基线
    baseline_values = baseline_avg + baseline_avg[:1]
    ax.plot(angles, baseline_values, '--', color='black', linewidth=linewidth, label='Original')

    ax.set_xticks(angles[:-1])
    ax.set_xticklabels(categories, fontsize=axis_font_size)
    # ax.set_title("Comparison of Code Style Transformation Methods Across Style Dimensions", fontsize=14)
    ax.legend(loc="upper right", bbox_to_anchor=(1.1, 1.1))  # legend 放在图右下方外部

    
    ax.set_ylim(2, 4)

    plt.savefig(output_file, format="pdf")
   
    plt.close()
    
    baseline_avg = {dim: np.mean(baseline_scores[dim]) for dim in categories}
    method_avg["baseline"] = baseline_avg

    # === 3. 导出到 CSV ===
    output_df = pd.DataFrame.from_dict(method_avg, orient="index")
    output_csv = os.path.join("results/score.csv")
    output_df.to_csv(output_csv, index_label="Method", float_format="%.4f")


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

        # 保存为 eps 矢量图可选 eps
        output_file = os.path.join(output_dir, f"{dim}_style_boxplot.pdf")
        plt.tight_layout()
        plt.savefig(output_file, format="pdf")
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
    plt.savefig(output_file, format="pdf")
    plt.close()

def plot_overall_success_rate(output_file: str):
    methods = ["egsi", "codebuff", "deepseek-r1-0528--free", "gpt-4.1"]

    # Forsee 结果
    forsee_rates = [41.38, 46.55, 31.03, 46.55]

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
    plt.savefig(output_file, format="pdf")
    plt.close()
    
import csv
import json
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score

def evaluate_forsee(input_csv: str, output_csv: str, positive_label=True, flag="all"):
    """
    从 CSV 文件读取任务数据，计算 Forsee 对各方法的预测指标，并写入 CSV。
    CSV 文件必须包含 success_map 和 forsee_result 字段，存成 JSON 字符串。
    """
    method_labels = {}  # 人类真实标签
    method_preds = {}   # Forsee预测标签

    with open(input_csv, "r", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            success_map = json.loads(row["success_map"])
            forsee_result = json.loads(row["forsee_result"])

            for method, human_label in success_map.items():
                forsee_label_str = forsee_result.get(method, "Failure-Modification")
                human_label_int = 1 if human_label == positive_label else 0
                forsee_positive_label = "Success"  if positive_label == True else "Failure-Modification"
                forsee_label_int = 1 if forsee_label_str == forsee_positive_label else 0

                if method not in method_labels:
                    method_labels[method] = []
                    method_preds[method] = []
                    
                if flag == "all" or (flag=="success" and human_label_int == 1) or (flag=="failure" and human_label_int == 1):
                    method_labels[method].append(human_label_int)
                    method_preds[method].append(forsee_label_int)
    sorted_methods = sorted(method_labels.keys())
    results = []
    for method in sorted_methods:
        y_true = method_labels[method]
        y_pred = method_preds[method]
        acc = accuracy_score(y_true, y_pred)
        precision = precision_score(y_true, y_pred, zero_division=0)
        recall = recall_score(y_true, y_pred, zero_division=0)
        f1 = f1_score(y_true, y_pred, zero_division=0)
        # success_rate = sum(y_pred) / len(y_pred)

        results.append({
            "Method": method,
            "Accuracy": acc,
            "Precision": precision,
            "Recall": recall,
            "F1": f1,
            # "Forsee_Success_Rate": success_rate
        })

    # 写入 CSV
    keys = ["Method", "Accuracy", "Precision", "Recall", "F1", "Forsee_Success_Rate"]
    with open(output_csv, "w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=keys)
        writer.writeheader()
        for row in results:
            writer.writerow(row)

import pandas as pd
import ast

# 输出人类判断失败但是forsee判断成功的

def find_false_positives(file_path: str, method: str = "codebuff"):
    
    def parse_str(s: str):
        """自动解析 success_map / forsee_result 字符串"""
        try:
            return ast.literal_eval(s)   # Python dict 格式 {'a': True}
        except Exception:
            return json.loads(s.replace("'", '"'))  # JSON 格式 {"a": true}
    df = pd.read_csv(file_path)
    false_tasks = {"format": [], "syntax": [], "semantic": []}


    for _, row in df.iterrows():
        success_map = parse_str(row["success_map"])
        forsee_result = parse_str(row["forsee_result"])

        human = success_map.get(method)
        forsee = forsee_result.get(method)

        # 转换 forsee 的结果到 True/False
        if forsee == "Success":
            forsee_bool = True
        elif forsee == "Failure-Modification":
            forsee_bool = False
        else:
            continue

        # 人类认为失败 & forsee 认为成功
        if human is False and forsee_bool is True:
            false_tasks["format"].append(json.loads(row["format_score_map"])["original_src"])
            false_tasks["syntax"].append(json.loads(row["syntax_score_map"])["original_src"])
            false_tasks["semantic"].append(json.loads(row["semantic_score_map"])["original_src"])


    for k, v in false_tasks.items():
        print(k)
        print(np.median(list(v)))

    print(false_tasks)


def merge_csv_files(input_dir, output_file):
    dfs = []
    results = []

    # === 1. 读取并合并所有 CSV 文件 ===
    for file in os.listdir(input_dir):
        if file.endswith(".csv"):
            file_path = os.path.join(input_dir, file)
            df = pd.read_csv(file_path)
            dfs.append(df)

    merged_df = pd.concat(dfs, ignore_index=True)

    # 删除“答题时长”小于 5 分钟的数据（假设单位为秒）
    merged_df["答题时长"] = pd.to_numeric(merged_df["答题时长"], errors="coerce")
    merged_df = merged_df[merged_df["答题时长"] >= 5 * 60]

    # === 2. 提取并格式化所需字段 ===
    print(len(merged_df))
    for _, row in merged_df.iterrows():
        # 安全取值函数
        def get_val(key):
            v = row.get(key, "")
            return str(v).strip() if pd.notna(v) else ""


        # 某些项可能是 "1. 初级" 这种格式，用 split(".") 拆出第二部分
        programming_experience = get_val("2.您的编程经验")
        if "." in programming_experience:
            programming_experience = programming_experience.split(".", 1)[1].strip()

        konw_java = get_val("3.您之前是否接触或使用过Java")
        if "." in konw_java:
            konw_java = konw_java.split(".", 1)[1].strip()

        styles_cared_about = []
                # 测试答卷有效性的题目
        test_question_results = []
        # 每个 task 的数据
        task_data = {}  # task_id -> {success_map, format_score_map, syntax_score_map, semantic_score_map}
        for key, value in row.items():
            if not value or not key:
                continue
            
            if key and key.startswith("4.您比较关注哪方面的代码风格？"):
                if len(str(row[key]).split(".")) > 1:
                    value = str(row[key]).split(".")[1]
                    if "其他" in value:
                        value = "其他"
                    styles_cared_about.append(value)

        results.append({
            "programming_experience": programming_experience,
            "konw_java": konw_java,
            "styles_cared_about": styles_cared_about
        })

    # === 3. 保存结果 ===
    if results:
        keys = results[0].keys()
        with open(output_file, "w", newline="", encoding="utf-8-sig") as f:
            writer = csv.DictWriter(f, fieldnames=keys)
            writer.writeheader()
            writer.writerows(results)

    print(f"✅ 合并完成，共生成 {len(results)} 条有效记录：{output_file}")


    

def output_agreement(input_csv, output_csv):
    """
    计算人类和 Forsee 在各方法下的四种一致性情况：
    Agree-Success, Agree-Failure, Human-Only, Forsee-Only, 以及 Total
    
    - success_map: {"egsi": false, "codebuff": false, "deepseek-r1-0528--free": false, "gpt-4.1": true}
    - forsee_result: {"egsi": "Success", "codebuff": "Failure-Modification", "deepseek-r1-0528--free": "Failure", "gpt-4.1": "Success"}
    """

    # === Step 1: 读取并解析 JSON ===
    df = pd.read_csv(input_csv)

    def parse_json(cell):
        """安全解析 JSON 格式"""
        if isinstance(cell, dict):
            return cell
        try:
            return json.loads(cell)
        except Exception:
            try:
                return json.loads(cell.strip('"').strip("'"))
            except Exception as e:
                print("❌ JSON parse error:", cell)
                raise e

    df["success_map"] = df["success_map"].apply(parse_json)
    df["forsee_result"] = df["forsee_result"].apply(parse_json)

    # === Step 2: 值转换 ===
    def normalize_human_value(v):
        if isinstance(v, bool):
            return int(v)
        if isinstance(v, str):
            v = v.strip().lower()
            return 1 if v == "true" else 0
        return int(bool(v))

    def normalize_forsee_value(v):
        if isinstance(v, str):
            v = v.strip().lower()
            return 1 if v.startswith("success") else 0
        return int(bool(v))

    # === Step 3: 初始化统计表 ===
    methods = list(df["success_map"].iloc[0].keys())
    case_types = ["Agree-Success", "Agree-Failure", "Human-Only", "Forsee-Only"]
    stats = {m: {ct: 0 for ct in case_types} for m in methods}

    # === Step 4: 统计一致性 ===
    for _, row in df.iterrows():
        human_dict = row["success_map"]
        forsee_dict = row["forsee_result"]

        for m in methods:
            if m not in human_dict or m not in forsee_dict:
                continue

            h = normalize_human_value(human_dict[m])
            f = normalize_forsee_value(forsee_dict[m])

            if h == 1 and f == 1:
                stats[m]["Agree-Success"] += 1
            elif h == 0 and f == 0:
                stats[m]["Agree-Failure"] += 1
            elif h == 1 and f == 0:
                stats[m]["Human-Only"] += 1
            elif h == 0 and f == 1:
                stats[m]["Forsee-Only"] += 1

    # === Step 5: 汇总 Total ===
    result = pd.DataFrame.from_dict(stats, orient="index")
    result["Total"] = result.sum(axis=1)

    # === Step 6: 保存结果 ===
    result.to_csv(output_csv, index_label="method")
    print(f"✅ Agreement summary saved to {output_csv}")
    return result


def compute_overlap_to_csv(input_csv, output_csv="./overlap_results/overlap_combined.csv"):
    import itertools

    """
    计算 Human-Only 与 Forsee-Only 的任务重叠矩阵，
    并将它们输出到同一个 CSV 文件（两个表之间留空行）。
    
    参数：
        input_csv : str
            输入文件路径，包含 success_map 和 forsee_result 两列
        output_csv : str
            输出文件路径（默认为 ./overlap_results/overlap_combined.csv）
    """
    # === 1. 读取输入 ===
    df = pd.read_csv(input_csv)

    # === 2. 解析 JSON ===
    df["success_map"] = df["success_map"].apply(json.loads)
    df["forsee_result"] = df["forsee_result"].apply(json.loads)

    # === 3. 获取方法列表 ===
    methods = list(df["success_map"].iloc[0].keys())

    # === 4. 构建集合 ===
    human_only = {m: set() for m in methods}
    forsee_only = {m: set() for m in methods}

    for idx, row in df.iterrows():
        human = row["success_map"]
        forsee = row["forsee_result"]

        for m in methods:
            h = human[m]
            f = forsee[m]

            # Human-Only: 人类成功, Forsee失败
            if h and not f.startswith("Success"):
                human_only[m].add(idx)

            # Forsee-Only: 人类失败, Forsee成功
            elif not h and f.startswith("Success"):
                forsee_only[m].add(idx)

    # === 5. 计算重叠矩阵 ===
    def compute_overlap_matrix(case_dict):
        matrix = pd.DataFrame(index=methods, columns=methods, dtype=int)
        for m1, m2 in itertools.product(methods, methods):
            matrix.loc[m1, m2] = len(case_dict[m1] & case_dict[m2])
        return matrix

    human_overlap = compute_overlap_matrix(human_only)
    forsee_overlap = compute_overlap_matrix(forsee_only)

    # === 6. 输出到同一个文件 ===
    os.makedirs(os.path.dirname(output_csv), exist_ok=True)
    with open(output_csv, "w", encoding="utf-8") as f:
        f.write("# ===== Human-Only Overlap =====\n")
        human_overlap.to_csv(f, index_label="method")
        f.write("\n\n# ===== Forsee-Only Overlap =====\n")
        forsee_overlap.to_csv(f, index_label="method")

    print(f"✅ Combined overlap file saved to: {output_csv}")
    return human_overlap, forsee_overlap

if __name__ == "__main__":
    results_dir = os.path.join(HUMAN_STUDY_DIR, "results")
    raw_data_path = os.path.join(results_dir, "processed_data.csv")
    data_path = os.path.join(results_dir, "data.csv")
    questionnar_data_dir = os.path.join(HUMAN_STUDY_DIR, "data")
    graphs_dir = os.path.join(results_dir, "graphs")
    # find_false_positives(data_path)
    
    # preprocess(questionnar_data_dir, raw_data_path)
    # process(raw_data_path, data_path)
    
    # graphs_dir = os.path.join(results_dir, "graphs")
    basic_info_graph(raw_data_path, graphs_dir)
    basic_info_to_csv(os.path.join(results_dir, "merged_data.csv"), os.path.join(results_dir, "basic_info.csv"))
    success_rate_data = cal_success_rate(raw_data_path, os.path.join(results_dir, "success_rates.csv"))
    plot_score_radar(data_path, os.path.join(graphs_dir, "score_radar.pdf"))
    # plot_method_boxplot(data_path, graphs_dir)
    
    # plot_success_rate(success_rate_data, os.path.join(graphs_dir, "success_rate.pdf"))
    # plot_overall_success_rate(os.path.join(graphs_dir, "overall_success_rate.pdf"))
    
    # 计算forsee对于各个方法成功率和失败率的预测结果
    output_agreement(data_path, os.path.join(results_dir, "agreements.csv"))
    compute_overlap_to_csv(data_path, os.path.join(results_dir, "deviation_overlap.csv"))
    # evaluate_forsee(data_path, os.path.join(results_dir, "forsee_metrics.csv"))
    # evaluate_forsee(data_path, os.path.join(results_dir, "forsee_success_metrics.csv"),True, "success")
    # evaluate_forsee(data_path, os.path.join(results_dir, "forsee_failure_metrics.csv"), False, "failure")
    merge_csv_files(questionnar_data_dir, os.path.join(results_dir, "merged_data.csv"))