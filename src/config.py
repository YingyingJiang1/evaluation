import json
import os

TOP_PERCENT_AUTHOR = 0.25

class ProjectConfig:
    def __init__(self, name, repo_path, file_tag_output_csv, method_tag_output_csv,
                 allowed_suffix='.java', include_dirs=[],top_percent=TOP_PERCENT_AUTHOR, build_tool="gradle", jars= [], modules=[]):
        self.name = name
        self.repo_path = repo_path
        self.file_tag_output_csv = file_tag_output_csv
        self.method_tag_output_csv = method_tag_output_csv
        self.allowed_suffix = allowed_suffix
        self.top_percent = top_percent
        self.include_dirs = include_dirs
        self.build_tool = build_tool
        self.jars = ";".join([os.path.join(repo_path, reletive_jar) for reletive_jar in jars])
        self.modules = modules
        
        self._populate_include_dirs()
        # print(self.include_dirs)

    def _populate_include_dirs(self):
        for root, dirs, files in os.walk(self.repo_path):
            # 统一路径分隔符为 /
            norm_root = root.replace(os.sep, '/')
            if norm_root.endswith('src/main'):
                self.include_dirs.append(norm_root.replace(self.repo_path + "/", ""))

    def __str__(self):
        return (f"ProjectConfig(name={self.name}, repo_path={self.repo_path}, "
                f"file_tag_output_csv={self.file_tag_output_csv}, method_tag_output_csv={self.method_tag_output_csv}, allowed_suffix={self.allowed_suffix}, "
                f"include_dirs={self.include_dirs}),top_percent={self.top_percent}")

    @classmethod
    def from_dict(cls, config_dict):
        return cls(
            name=config_dict['name'],
            repo_path=config_dict['repo_path'],
            file_tag_output_csv=config_dict['file_tag_output_csv'],
            method_tag_output_csv=config_dict['method_tag_output_csv'],
            allowed_suffix=config_dict.get('allowed_suffix', '.java'),
            include_dirs=config_dict.get('include_dirs', []),
            top_percent=config_dict.get('top_percent', TOP_PERCENT_AUTHOR),
            build_tool=config_dict.get('build_tool', "maven"),
            jars=config_dict.get('jars', []),
            modules=config_dict.get('modules', [])
        )


class ProjectConfigs:
    def __init__(self, json_file='projects-config.json'):
        self.json_file = json_file
        self.projects = None
        self.load_configs(json_file)

    def load_configs(self, json_file):
        with open(json_file, 'r', encoding='utf-8') as f:
            configs = json.load(f)
            self.projects = [ProjectConfig.from_dict(config) for config in configs]

    def get_project_by_name(self, name) -> ProjectConfig:
        return next((c for c in self.projects if c.name == name), None)

    def get_all_project_names(self):
        return [project.name for project in self.projects]

    def __str__(self):
        return "\n".join([str(project) for project in self.projects])

