from transform import ResultManager, TransformReuslt
from dataset import TransformationData, Data, TransformPair
from utils import *
from config import ProjectConfigs
import re

import json

from eval import DataCache, create_pair_dict


def update_pair_id(old_data:list[TransformPair], new_data:list[TransformPair]):
    old_dict = {}
    for d in old_data:
        old_dict[d.pair_id] = d
        
    new_dict = {}
    for d in new_data:
        new_dict[(d.src_id, ",".join(sorted(d.target_ids)))] = d.pair_id
        
    new_result = ResultManager(res_path.replace("-old.jsonl", ".jsonl"))
    for r in result_manager.get_all_results():
        key = (old_dict[r.pair_id].src_id, ",".join(sorted(old_dict[r.pair_id].target_ids)))
        if key in new_dict:
            new_id = new_dict[key]
            r.pair_id = new_id
            new_result.add_results([r])
    
    new_result.flush_all()
        

# res_path = "../data/200/deepseek-r1-0528--free-result.jsonl"
# result_manager= ResultManager(res_path)
# result_manager.output_file = res_path.replace(".jsonl", "-new.jsonl")
# result_manager.update_all()

# update_pair_id(old_data, new_data)
                
                
                
def fix_model_results(filepath, min_target_lines):
    from eval import create_pair_dict, DataCache

    result_manager= ResultManager(filepath)
    
    pair_dict = create_pair_dict(min_target_lines, ["across-project"])
    
            
    for r in result_manager.get_all_results():
        if "," in r.pair_id:
            with open("result-tmp.java", "w", encoding="utf8") as f:
                f.write(r.code)
                
            if os.path.exists("src-tmp.java"):
                os.remove("src-tmp.java")
            with open("src-tmp.java", "a", encoding="utf8") as f:
                src_ids = r.src_id.split(",")
                pair_ids = r.pair_id.split(",")
                for pair_id in pair_ids:
                    src_id = src_ids[pair_ids.index(pair_id)]
                    code = DataCache.get_code_list(r.project_name, pair_dict[r.project_name, pair_id].src_author, [src_id])[0]
                    f.write(f"// id:{pair_id}\n")
                    f.write(code)
                    f.write("\n")
            
            # 输入 pair_id start_line end_line，挨个处理
            src_ids = r.src_id.split(",")
            pair_ids = r.pair_id.split(",")
            with open("result-tmp.java", "r", encoding="utf8") as f:
                codelines = f.readlines()
                input_str = input()
                while input_str != "q":
                    pair_id, start_line, end_line = input_str.split(' ')
                    src_id = src_ids[pair_ids.index(pair_id)]
                    result = TransformReuslt(r.project_name, pair_id, src_id, "".join(codelines[int(start_line) - 1 : int(end_line)]))
                    print(f"result:\n{result.code}")                    
                    result_manager.add_results([result])
                    
                    input_str = input()
                    
    result_manager.flush_all()
    


def update_order(filepath):
    result_manager= ResultManager(filepath)
    result_manager.update_all()


def sort_results_by_pair_id():
    dir = os.path.dirname(create_transformation_result_jsonl_path("", 200))
    for file in os.listdir(dir):
        if "result" not in file:
            continue
        path = os.path.join(dir, file)
        res_manager = ResultManager(path)
        res_manager.result_dict = dict(sorted(res_manager.result_dict.items(), key=lambda x: int(x[0][1])))
        res_manager.update_all()


def fix_order_of_results():
    from eval import create_pair_dict, DataCache
    from author_tagger import JavaTreeSitterParser

    min_target_lines = 200
    pair_dict = create_pair_dict(min_target_lines, ["across-project"])

    dir = os.path.dirname(create_transformation_result_jsonl_path("", min_target_lines))
    for file in os.listdir(dir):
        if "result" not in file:
            continue
        path = os.path.join(dir, file)
        res_manager = ResultManager(path)
        res_manager.get_all_results()

        data_dict = {} # key:(src_author, target_author)
        for r in res_manager.get_all_results():
            pair = pair_dict[r.pair_id]
            key = (pair.src_author, pair.target_author)
            if key not in data_dict:
                data_dict[key] = []
            data_dict[key].append(r)

        for (src_author, target_author), result_list in data_dict:
            parser = JavaTreeSitterParser("")
            result_codes = {} # signature:code
            conflicts = set()
            for r in result_list:
                parser.parse_code_str(r.code)
                signature = parser.get_method_signature()[0]
                if signature in result_codes:
                    conflicts.add(signature)
                result_codes[signature] = r.code
            for signature in conflicts:
                del result_codes[signature]
            if len(result_codes) != len(result_list):
                print(f"Exists Same Signature, {src_author}-{target_author}")

            for r in result_list:
                original_code = DataCache.get_code_list(r.project_name, src_author, [r.src_id])
                parser.parse_code_str(original_code)
                signature = parser.get_method_signature()[0]
                if result_codes[signature]:
                    r.code = result_codes[signature].code
            pass
            
        res_manager.update_all()
    pass


# fix_model_results("/data1/jyy/style/evaluation/data/across-project/400/gpt-4.1-result.jsonl", 400)

            
# old_pairs = []
# with open("/data1/jyy/style/evaluation/data/across-project-backup/200/across-project-pairs.jsonl", "r", encoding="utf-8") as f:
#     for line in f:
#         if line.strip():                  # 跳过空行
#             old_pairs.append(TransformPair.from_dict(json.loads(line)))
# old_pair_dict = {p.pair_id:p for p in old_pairs}

# res_dir = "/data1/jyy/style/evaluation/data/across-project-backup/200"

# from author_tagger import AuthorIDMap

# new_id_map = AuthorIDMap()
# new_id_map.load()

# old_id_map = AuthorIDMap()
# old_id_map.load("/data1/jyy/style/evaluation/data/across-project-backup/author-id-map-old.jsonl")

# for file in os.listdir(res_dir):
#     if "result" not in file or "deepseek" in file:
#         continue
#     res_path = os.path.join(res_dir, file)
#     print(f"loading {res_path}...")
#     result_manager= ResultManager(res_path)
#     new_manager = ResultManager(os.path.join("/data1/jyy/style/evaluation/data/across-project/200", file))

#     for key, result in result_manager.result_dict.items():
#         old_pair_id = key[1]
#         old_pair = old_pair_dict[old_pair_id]
#         old_src_author, old_target_author = old_pair.src_author, old_pair.target_author
        
#         new_src_author = new_id_map.get_author_id(old_id_map.get_author_project(old_src_author), old_id_map.get_author_name(old_src_author))
#         new_target_author = new_id_map.get_author_id(old_id_map.get_author_project(old_target_author), old_id_map.get_author_name(old_target_author))

#         new_pair = None
#         for p in new_pairs:
#             if p.src_author == new_src_author and p.target_author == new_target_author and p.src_id == old_pair.src_id and p.target_ids == old_pair.target_ids:
#                 new_pair = p
#                 break

        
#         if new_pair:
#             new_manager.add_results([TransformReuslt(new_pair.project_name, new_pair.pair_id, new_pair.src_id, result.code)])

def update_test_results(min_target_lines, method="egsi"):
    file = create_transformation_result_jsonl_path(method, min_target_lines)
    tested_file = file.replace(".jsonl", "-old.jsonl")
    manager = ResultManager(file)
    tested_manager = ResultManager(tested_file)
    
    print(tested_file)
    
    for r in manager.get_all_results():
        tested_r = tested_manager.get_result(r.project_name, r.pair_id)
        # 结果相同，同步测试结果
        if tested_r and r.code == tested_r.code:
            r.compilable = tested_r.compilable
            r.test_passed = tested_r.test_passed
            
    manager.update_all()
    

    
    
def count_different_src_files():
    pair_dict = create_pair_dict(200, ["across-project"])
    file_set = set()
    for p in pair_dict.values():
        file_set.add(p.src_author + p.src_id)
    print(len(file_set))

if __name__ == "__main__":
    
    # methods = [
    #         "egsi",
    #         "codebuff",
    #         "deepseek-r1-0528--free",
    #         "gpt-4.1",
    #         # "claude-3.7-sonnet"
    #     ]
    # update_test_results(200)
    
    # for m in methods:
    #     print_failed_tests(200, m)
    # result = ResultManager(create_transformation_result_jsonl_path("egsi", 200))
    # print(result.get_result("across-project", "328").code)
    
    count_different_src_files()

