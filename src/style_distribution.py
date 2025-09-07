import xml.etree.ElementTree as ET
import matplotlib.pyplot as plt
import ast
import os
from collections import defaultdict
from typing import Dict, List


def parse_statistic_string(stat_str: str) -> Dict[int, int]:
    """将字符串形式的 statistic map 转换为字典"""
    stat_str = stat_str.replace('=', ':')
    try:
        return ast.literal_eval(stat_str)
    except Exception as e:
        print(f"Error parsing statistic string: {stat_str}")
        raise e


def extract_styles_statistics(xml_path: str) -> Dict[str, Dict[int, Dict[int, int]]]:
    """
    提取所有 style 的统计信息。
    返回格式: {style_name: {context_number: {key: frequency}}}
    """
    tree = ET.parse(xml_path)
    root = tree.getroot()

    all_style_stats = {}

    for style_elem in root.findall('./style'):
        style_name = style_elem.attrib.get('name', 'unnamed')
        context_stats = defaultdict(lambda: defaultdict(int))

        for rule in style_elem.findall('./rule'):
            context_elem = rule.find('context')
            prop_elem = rule.find('property')

            context_num = int(context_elem.attrib['number'])
            stat_map = parse_statistic_string(prop_elem.attrib['statistic'])

            for k, v in stat_map.items():
                context_stats[context_num][int(k)] += int(v)

        all_style_stats[style_name] = context_stats

    return all_style_stats


def prepare_stacked_data(context_stats: Dict[int, Dict[int, int]]):
    """准备堆叠柱状图的数据"""
    all_keys = sorted({k for stats in context_stats.values() for k in stats})
    contexts = sorted(context_stats.keys())

    data_per_key = {k: [] for k in all_keys}
    for ctx in contexts:
        stats = context_stats[ctx]
        for k in all_keys:
            data_per_key[k].append(stats.get(k, 0))

    return contexts, all_keys, data_per_key


def plot_stacked_bar(contexts: List[int], data_per_key: Dict[int, List[int]],
                     all_keys: List[int], title: str, save_path: str = None):
    """绘制横向堆叠柱状图"""
    fig, ax = plt.subplots(figsize=(10, 6))
    left = [0] * len(contexts)

    for k in all_keys:
        freqs = data_per_key[k]
        ax.barh(contexts, freqs, left=left, label=f'Value {k}')
        left = [l + f for l, f in zip(left, freqs)]

    ax.set_ylabel('Context Number')
    ax.set_xlabel('Frequency')
    ax.set_title(title)
    ax.legend(title='Value')
    plt.yticks(contexts)
    plt.tight_layout()

    if save_path:
        plt.savefig(save_path)
        print(f"Saved: {save_path}")
    else:
        plt.show()
        
from typing import List, Dict
import matplotlib.pyplot as plt

def plot_stacked_percent_bar(contexts: List[int], data_per_key: Dict[int, List[int]],
                     all_keys: List[int], title: str, save_path: str = None):
    """绘制横向堆叠柱状图（百分比）"""
    fig, ax = plt.subplots(figsize=(10, 6))
    left = [0] * len(contexts)

    # 计算每个 context 的总和
    total_per_context = [0] * len(contexts)
    for k in all_keys:
        for i, val in enumerate(data_per_key[k]):
            total_per_context[i] += val

    for k in all_keys:
        freqs = data_per_key[k]
        # 转换为百分比
        percent_freqs = [
            (f / total) * 100 if total != 0 else 0
            for f, total in zip(freqs, total_per_context)
        ]
        ax.barh(contexts, percent_freqs, left=left, label=f'Value {k}')
        left = [l + p for l, p in zip(left, percent_freqs)]

    ax.set_ylabel('Context Number')
    ax.set_xlabel('Percentage (%)')
    ax.set_title(title)
    ax.legend(title='Value')
    plt.yticks(contexts)
    plt.tight_layout()

    if save_path:
        plt.savefig(save_path)
        print(f"Saved: {save_path}")
    else:
        plt.show()



def main(xml_path: str, output_dir: str = None, project_name=""):
    style_stats = extract_styles_statistics(xml_path)

    for style_name, context_stats in style_stats.items():
        contexts, all_keys, data_per_key = prepare_stacked_data(context_stats)
        title = f'Statistic Distribution for Style "{style_name}"'

        if output_dir:
            os.makedirs(output_dir, exist_ok=True)
            save_path = os.path.join(output_dir, f'{project_name}_{style_name}.png')
        else:
            save_path = None

        plot_stacked_percent_bar(contexts, data_per_key, all_keys, title, save_path)


# 示例调用方式
if __name__ == '__main__':
    xml_file = '../style-statistic.xml'          # 替换为你的 XML 路径
    output_directory = 'output_charts'  # 输出图表目录
    main(xml_file, output_directory, "stirlingpdf")
