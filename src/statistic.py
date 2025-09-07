import os
import matplotlib.pyplot as plt
import numpy as np
import csv
import pandas as pd
import math

from path import META_DATA_DIR
from utils import *
from config import ProjectConfigs
from dataset import Data, MethodWrapper
from typing import Dict, List

# 输出每个作者的代码行规模（去除注释和空行后的代码行）
def show_code_size_distribution():
    rows = []
    for project in ProjectConfigs().projects:
        data = Data(project)
        for author, data_list in data.data_dict.items():
            total_lines = sum([d.get_only_code_lines_count() if d.get_only_code_lines_count() >= 20 else 0 for d in data_list])
            if total_lines > 0:
                rows.append({
                    "project": project.name,
                    "author": author,
                    "code_lines": total_lines
                })

    output_path = os.path.join(META_DATA_DIR, "csv", "code-size-distribution.csv")
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    with open(output_path, "w", encoding="utf-8", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=["project", "author", "code_lines"])
        writer.writeheader()
        writer.writerows(rows)
    

def collect_java_stats(root_dir):
    author_file_counts = {}
    author_avg_lines = {}
    author_total_lines = {}

    if not os.path.exists(root_dir):
        return {}, {}, {}
    for author in os.listdir(root_dir):
        author_dir = os.path.join(root_dir, author)
        if not os.path.isdir(author_dir):
            continue

        total_lines = 0
        file_count = 0

        for file in os.listdir(author_dir):
            if file.endswith(".java"):
                file_path = os.path.join(author_dir, file)
                with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
                    lines = f.readlines()
                    total_lines += len(lines)
                    file_count += 1

        author_total_lines[author] = total_lines
        author_file_counts[author] = file_count
        author_avg_lines[author] = (total_lines / file_count) if file_count > 0 else 0

    return author_file_counts, author_avg_lines, author_total_lines



def show_distribution(file_counts, avg_lines, output_path):
    # 根据 file_counts 从小到大排序
    sorted_items = sorted(file_counts.items(), key=lambda item: item[1])
    authors = [item[0] for item in sorted_items]
    file_nums = [item[1] for item in sorted_items]
    avg_line_nums = [avg_lines[a] for a in authors]

    x = np.arange(len(authors))

    fig, ax1 = plt.subplots(figsize=(14, 6))

    # 柱状图
    bars = ax1.bar(x, file_nums, color='skyblue', label='File Count')
    ax1.set_ylabel("Number of Java Files", color='skyblue')
    ax1.tick_params(axis='y', labelcolor='skyblue')

    for bar in bars:
        height = bar.get_height()
        ax1.annotate(f'{height}',
                     xy=(bar.get_x() + bar.get_width() / 2, height),
                     xytext=(0, 3),
                     textcoords="offset points",
                     ha='center', va='bottom', fontsize=8)

    # 折线图，使用第二个 y 轴
    ax2 = ax1.twinx()
    ax2.plot(x, avg_line_nums, color='orange', marker='o', label='Average Lines per File')
    ax2.set_ylabel("Average Lines per File", color='orange')
    ax2.tick_params(axis='y', labelcolor='orange')

    for i, y in enumerate(avg_line_nums):
        ax2.annotate(f'{y:.1f}', xy=(x[i], y), xytext=(0, 3),
                     textcoords="offset points", ha='center', va='bottom', fontsize=8)

    # 统一横轴设置
    ax1.set_xlabel("Author")
    ax1.set_title("Java File Count and Average Lines per File (Sorted by File Count)")
    ax1.set_xticks(x)
    ax1.set_xticklabels(authors, rotation=45, ha="right")

    fig.tight_layout()
    
    if not os.path.exists(os.path.dirname(output_path)):
        os.makedirs(os.path.dirname(output_path), exist_ok=True)
        
    plt.savefig(output_path)
    plt.close()
    
def save_meta_to_csv(project_name, dataset_category, author_counts, author_avg_lines, author_total_lines):
    meta_path = os.path.join(META_DATA_DIR, "csv", f'{dataset_category}-meta.csv')
    if not os.path.exists(os.path.dirname(meta_path)):
        os.makedirs(os.path.dirname(meta_path), exist_ok=True)

    authors = list(author_counts.keys())
    total_authors = len(authors)
    total_files = sum(author_counts.values())
    
    file_counts = list(author_counts.values())
    avg_lines = list(author_avg_lines.values())
    total_lines = list(author_total_lines.values())

    avg_file_count = np.mean(file_counts) if total_authors > 0 else 0
    avg_line_count = np.mean(avg_lines) if total_authors > 0 else 0
    avg_lines_per_author = np.mean(total_lines) if total_authors > 0 else 0
    median_lines_per_author = np.median(total_lines) if total_authors > 0 else 0
    file_count_var = np.var(file_counts) if total_authors > 0 else 0
    avg_lines_var = np.var(avg_lines) if total_authors > 0 else 0

    header = [
        'Project Name', 'Total Authors', 'Total Files', 'Avg Files per Author', 
        'Avg Lines per File', 'Avg Lines per Author', 'Median Lines per Author',
        'File Count Variance', 'Avg Lines Variance'
    ]

    write_header = not os.path.exists(meta_path)

    with open(meta_path, 'a', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        if write_header:
            writer.writerow(header)
        writer.writerow([
            project_name, total_authors, total_files, avg_file_count,
            avg_line_count, avg_lines_per_author, median_lines_per_author,
            file_count_var, avg_lines_var
        ])

class Info:
    def __init__(self, project_name, data_category, codes_dir):
        self.project_name = project_name
        self.data_category = data_category
        self.codes_dir = codes_dir
        
        
def plot_bar_chart(data_dict, title, output_path, xlabel="Author", ylabel="Total Lines"):
    keys = list(data_dict.keys())
    values = list(data_dict.values())

    plt.figure(figsize=(10, 6))
    bars = plt.bar(keys, values, color='skyblue')

    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)

    # 添加数据标签
    for bar in bars:
        height = bar.get_height()
        plt.text(bar.get_x() + bar.get_width() / 2, height,
                 f'{height:.1f}', ha='center', va='bottom', fontsize=10)

    plt.xticks(rotation=45, ha='right')
    plt.tight_layout()
    plt.grid(axis='y', linestyle='--', alpha=0.7)

    plt.savefig(output_path)
    plt.close()  


def show_total_lines_stats(total_lines):
    if not total_lines:
        print("No data available.")
        return
    
    lines = list(total_lines.values())
    min_lines = min(lines)
    max_lines = max(lines)
    avg_lines = sum(lines) / len(lines)
    median_lines = np.median(lines)

    print(f"Min lines: {min_lines}")
    print(f"Max lines: {max_lines}")
    print(f"Avg lines: {avg_lines:.2f}")
    print(f"Median lines: {median_lines}")



def show_raw_data_stats(project_config): 
    def cal_stats(data_dict):
        total_lines = []

        for author, results in data_dict.items():
            line_counts = [r.get_line_count() for r in results if r.get_line_count() > 0]
            if not line_counts:
                continue
            total = sum(line_counts)
            total_lines.append(total)

        if not total_lines:
            print("No line data to analyze.")
            return None

        # 按行数降序排列
        total_lines = sorted(total_lines, reverse=True)

        def get_min_top_percent(percent):
            count = math.ceil(len(total_lines) * percent)
            top_lines = total_lines[:count]
            return top_lines[-1] if top_lines else 0

        stats = {
            "author_count": len(total_lines),
            "min_top_20": get_min_top_percent(0.2),
            "min_top_25": get_min_top_percent(0.25),
            "min_top_30": get_min_top_percent(0.3),
            "min_top_35": get_min_top_percent(0.35),
            "min_top_40": get_min_top_percent(0.40),
        }
        return stats

    data = Data(project_config)

    # 获取两类数据的统计
    full_data_stats = cal_stats(data.data_dict)
    training_data_stats = cal_stats(data.training_data)
    transformation_stats = cal_stats(data.transformation_data)

    # 整理为DataFrame
    rows = []
    if training_data_stats:
        rows.append({"data_type": "training_data", **training_data_stats})
    if transformation_stats:
        rows.append({"data_type": "transformation_data", **transformation_stats})
    if transformation_stats:
        rows.append({"data_type": "full_data", **full_data_stats})

    df = pd.DataFrame(rows, columns=[
        "data_type", "author_count", "min_top_20", "min_top_25", "min_top_30", "min_top_35", "min_top_40"
    ])

    # 保存为CSV
    output_path = create_raw_data_meta_csv_path(project_config.name)
    result_dir = os.path.dirname(output_path)
    os.makedirs(result_dir, exist_ok=True)

    df.to_csv(output_path, index=False)
    print(f"Saved raw code statistics to: {output_path}")
    

def main():
    info_list = []
    java40_info = Info("java40", "forsee", 'java40/java40')
    info_list.append(java40_info)
    

    configs = ProjectConfigs()
    for project in configs.projects:
        info = Info(project.name, project.name, create_codes_dir(project.name))
        info_list.append(info)
        
        
    
    for info in info_list:
        project_name, data_category, codes_dir = info.project_name, info.data_category, info.codes_dir
        
        file_counts, avg_lines, total_lines = collect_java_stats(codes_dir)
        
        print(f"---{project_name}---")
        show_total_lines_stats(total_lines)
        print()
        
        # print(total_lines)
        # output_path = os.path.join(META_DATA_DIR, "png", f"{project_name}-{data_category}.png")
        # show_distribution(file_counts, avg_lines, output_path)
        # save_meta_to_csv(project_name, data_category, file_counts, avg_lines, total_lines)
        
        # plot_bar_chart(total_lines, "Total Lines per Author", output_path= os.path.join(META_DATA_DIR, "png", f"{project_name}-{data_category}-total-lines.png"))


    
def count_selected_author():
    from author_tagger import AuthorIDMap
    from dataset import TransformationData
    from collections import Counter
    
    data = TransformationData("across-project", None).load_data()
    ids = set()
    for d in data:
        ids.add(d.src_author)
        ids.add(d.target_author)
    
    project_counts = []
    id_map = AuthorIDMap()
    for d in ids:
        project_counts.append(id_map.get_author_project(d))
    print(Counter(project_counts))

if __name__ == "__main__":
    show_code_size_distribution()
    # count_selected_author()

    for project in ProjectConfigs().projects:
        data_list = Data(project).load_data()
        total_lines = sum([r.get_line_count() for r in data_list])
        print(f"{project.name}:{total_lines}")
    
    