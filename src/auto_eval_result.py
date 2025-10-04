import pandas as pd
import matplotlib.pyplot as plt
from utils import *

import pandas as pd
import matplotlib.pyplot as plt

def success_rate_by_threshold_across_methods(files_dict: dict[int, str], output_file: str, csv_output: str):
    thresholds = sorted(files_dict.keys())
    
    method_rates = {}

    # 合并各个阈值的 Success Rate
    for threshold in thresholds:
        df = pd.read_csv(files_dict[threshold])
        for _, row in df.iterrows():
            method = row["Method"]
            rate = float(row["Success Rate"])
            method_rates.setdefault(method, {})[threshold] = rate

    # 转换为 DataFrame
    merged_df = pd.DataFrame.from_dict(method_rates, orient="index")
    merged_df.index.name = "Method"
    merged_df = merged_df.reset_index()
    
    # 增加平均值列
    merged_df["Average"] = merged_df[thresholds].mean(axis=1)
    merged_df[thresholds + ["Average"]] = merged_df[thresholds + ["Average"]].round(2)
    
    # 保存合并结果到 CSV
    merged_df.to_csv(csv_output, index=False)

    # -----------------画图部分-----------------
    methods = merged_df["Method"].tolist()
    min_rate = merged_df.drop(columns=["Method"]).min().min()
    max_rate = merged_df.drop(columns=["Method"]).max().max()
    y_margin = (max_rate - min_rate) * 0.05

    plt.figure(figsize=(10, 6))

    for threshold in thresholds:
        rates = merged_df[threshold].tolist()
        plt.plot(methods, rates, marker='o', linewidth=2, markersize=6, label=f"Threshold={threshold}")

    plt.xlabel("Method", fontsize=12)
    plt.ylabel("Success Rate (%)", fontsize=12)
    plt.title("Success Rate Across Methods and Row Thresholds", fontsize=14)
    plt.ylim(0, 100)
    plt.grid(True, linestyle="--", alpha=0.5)
    plt.legend(loc="upper right", fontsize=10)
    plt.tight_layout()
    plt.savefig(output_file, format="png")
    plt.close()
    
def success_rate_by_method_across_thresholds(files_dict: dict[int, str], output_file: str, csv_output: str):
    thresholds = sorted(files_dict.keys())
    
    method_rates = {}

    # 合并各个阈值的 Success Rate
    for threshold in thresholds:
        df = pd.read_csv(files_dict[threshold])
        for _, row in df.iterrows():
            method = row["Method"]
            rate = float(row["Success Rate"])
            method_rates.setdefault(method, {})[threshold] = rate

    # 转换为 DataFrame
    merged_df = pd.DataFrame.from_dict(method_rates, orient="index")
    merged_df.index.name = "Method"
    merged_df = merged_df.reset_index()
    
    # 增加平均值列
    merged_df["Average"] = merged_df[thresholds].mean(axis=1)
    merged_df[thresholds + ["Average"]] = merged_df[thresholds + ["Average"]].round(2)
    
    # 保存合并结果到 CSV
    merged_df.to_csv(csv_output, index=False)

    # -----------------画图部分-----------------
    plt.figure(figsize=(10, 6))

    for _, row in merged_df.iterrows():
        method = row["Method"]
        rates = row[thresholds].values
        plt.plot(thresholds, rates, marker='o', linewidth=2, markersize=6, label=method)

    plt.xticks(thresholds)
    plt.xlabel("Threshold", fontsize=12)
    plt.ylabel("Success Rate (%)", fontsize=12)
    plt.title("Success Rate Across Thresholds by Method", fontsize=14)
    plt.ylim(0, 100)
    plt.grid(True, linestyle="--", alpha=0.5)
    plt.legend(loc="best", fontsize=10)
    plt.tight_layout()
    plt.savefig(output_file, format="png")
    plt.close()
    
if __name__ == "__main__":
    lines = [200, 400, 800, 1000]
    files_dict = {}
    for line in lines:
        files_dict[line] = create_eval_transform_success_rate_path(line)
    dir = os.path.join(EVAL_DIR, "result")
    os.makedirs(dir, exist_ok=True)

    success_rate_by_method_across_thresholds(files_dict, os.path.join(dir, "success_rate.png"),  os.path.join(dir, "success_rate.csv"))
