import os
import csv
from collections import defaultdict
from datetime import datetime
from git import Repo
import shutil

from config import ProjectConfig, ProjectConfigs

def extract_project(project_config:ProjectConfig):
    # 初始化 Git 仓库
    repo = Repo(project_config.repo_path)
    assert not repo.bare

    file_stats = defaultdict(lambda: {
        'mod_count': 0,
        'authors': set(),
        'last_modified_time': None,
        'lines': 0
    })

    main_branch = repo.head.reference
    for commit in repo.iter_commits(main_branch, paths=project_config.include_dirs):
        commit_date = datetime.fromtimestamp(commit.committed_date)
        author = commit.author.name
        for filepath in commit.stats.files.keys():
            if not filepath.endswith(project_config.allowed_suffix):
                continue
            file_stats[filepath]['mod_count'] += 1
            file_stats[filepath]['authors'].add(author)
            existing_time = file_stats[filepath]['last_modified_time']
            if existing_time is None or commit_date > existing_time:
                file_stats[filepath]['last_modified_time'] = commit_date

    # 获取行数
    invalid_files = []
    for filepath in file_stats.keys():
        try:
            with open(os.path.join(project_config.repo_path, filepath), 'r', encoding='utf-8', errors='ignore') as f:
                file_stats[filepath]['lines'] = sum(1 for _ in f)
        except Exception as e:
            print(f"Error reading {filepath}: {e}")
            invalid_files.append(filepath)

    for filepath in invalid_files:
        del file_stats[filepath]

    # 构建结果
    results = []
    for filepath, stats in file_stats.items():
        results.append({
            'file': filepath,
            'mod_count': stats['mod_count'],
            'author_count': len(stats['authors']),
            'lines': stats['lines'],
            'last_modified_time': stats['last_modified_time'].strftime("%Y-%m-%d %H:%M:%S") if stats['last_modified_time'] else '',
        })

    # 排序
    results.sort(key=lambda x: (x['mod_count'], x['author_count'], x['lines']), reverse=True)

    # 写入 CSV
    with open(project_config.file_tag_output_csv, mode='w', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=['file', 'mod_count', 'author_count', 'last_modified_time', 'lines'])
        writer.writeheader()
        writer.writerows(results)

    print(f"\n✅ 只分析 {project_config.include_dirs} 中的 .java 文件，结果导出为：{project_config.file_tag_output_csv}")


def copy_top_n_files(csv_path, dest_dir, repo_path, top_n=10):
    if not os.path.exists(dest_dir):
        os.makedirs(dest_dir)

    with open(csv_path, 'r', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        count = 0
        for row in reader:
            file_rel_path = row['file']
            src_path = os.path.join(repo_path, file_rel_path)
            dest_path = os.path.join(dest_dir, os.path.basename(file_rel_path))

            if os.path.exists(src_path):
                shutil.copy2(src_path, dest_path)
                print(f"Copied: {file_rel_path} → {dest_path}")
                count += 1

            if count >= top_n:
                break


if __name__ == '__main__':
    # 读取项目配置
    configs = ProjectConfigs.load_configs('projects-config.json')
    # for project_config in configs.projects:
    #     extract_project(project_config)
    
    project_config  = configs.get_project_by_name("Stirling-PDF")
    copy_top_n_files(project_config.output_csv, os.path.join("../example", project_config.name, "srcs"), project_config.repo_path, top_n=5)





