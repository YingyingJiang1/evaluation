import pandas as pd
import matplotlib.pyplot as plt
from utils import *

def plot_success_rate_by_row_threshold(files_dict: dict[int, str], output_file: str):
    """
    绘制不同 code line threshold 下各方法的 Success Rate 折线图。
    
    Args:
        files_dict: dict[int, str]，key 为行数阈值，value 为对应 CSV 文件路径
        output_file: 输出文件路径 (建议 SVG)
    """
    plt.figure(figsize=(10, 6))
    
    # 定义颜色和 marker，保证不同阈值可区分
    colors = ["#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd", "#8c564b"]
    markers = ["o", "s", "^", "D", "v", "P"]
    
    for idx, (threshold, csv_file) in enumerate(sorted(files_dict.items())):
        df = pd.read_csv(csv_file)
        methods = df["Method"].tolist()
        rates = df["Success Rate"].tolist()
        plt.plot(methods, rates, label=f"line threshold={threshold}",
                 marker=markers[idx % len(markers)],
                 color=colors[idx % len(colors)],
                 linewidth=2, markersize=6)
    
    plt.ylabel("Success Rate (%)", fontsize=12)
    plt.xlabel("Method", fontsize=12)
    plt.title("Success Rate of Code Transformation Methods Across Table Sizes", fontsize=14)
    plt.xticks(rotation=45)
    plt.ylim(0, 20)
    plt.grid(True, linestyle="--", alpha=0.5)
    plt.legend(loc="lower right", fontsize=10)
    plt.tight_layout()
    
    # 保存矢量图
    plt.savefig(output_file, format="png")
    plt.close()

    
if __name__ == "__main__":
    lines = [200, 400, 800, 1000]
    files_dict = {}
    for line in lines:
        files_dict[line] = create_eval_transform_success_rate_path(line)
    dir = os.path.join(EVAL_DIR, "result")
    os.makedirs(dir, exist_ok=True)

    plot_success_rate_by_row_threshold(files_dict, os.path.join(dir, "success_rate.png"))