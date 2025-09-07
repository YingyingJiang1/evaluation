import subprocess
import csv
import re
import shutil
import threading

from dataclasses import dataclass, field, asdict

from config import *
from path import *
from utils import *
from multiprocessing import Process
from concurrent.futures import ThreadPoolExecutor

from openai import OpenAI
from transformers import AutoTokenizer, AutoModelForCausalLM
import torch
from transformers import (
    AutoModelForCausalLM,
    AutoTokenizer,
    GenerationConfig,
)
from myparser import JavaTreeSitterParser
from dataset import TransformationData, TransformPair, Data
from typing import List,Dict


@dataclass
class TransformPairPack:
    project_name:str
    pair_ids:List[str]
    src_author:str
    target_author:str
    src_ids:List[str]
    target_ids:List[str]

    
    def get_src_author(self):
        return self.src_author
    
    def get_target_author(self):
        return self.target_author
    
    def get_src_codes_dir(self):
        return os.path.join(create_src_codes_dir(self.project_name), f"{self.src_author}-{self.target_author}")

    #  同一个target author的代码是公用的
    def get_target_codes_dir(self):
        return os.path.join(create_target_codes_dir(self.project_name), self.target_author)
    
    def get_result_dir(self):
        return os.path.join(TMP_DATA, "results", self.src_author, self.target_author, )


    def remove_ids(self, target_pair_ids: List[str]):
        """
        根据给定的 pair_ids，移除对应的 pair_id 和 src_id。
        要求 pair_ids 和 src_ids 一一对应，按相同顺序保存。
        """
        if not target_pair_ids:
            return

        new_pair_ids = []
        new_src_ids = []

        for pair_id, src_id in zip(self.pair_ids, self.src_ids):
            if pair_id not in target_pair_ids:
                new_pair_ids.append(pair_id)
                new_src_ids.append(src_id)

        self.pair_ids = new_pair_ids
        self.src_ids = new_src_ids
        
    def get_src_id(self, pair_id):
        return self.src_ids[self.pair_ids.index(pair_id)]
    

    
    
@dataclass
class TransformReuslt:
    project_name:str
    pair_id:str
    src_id:str
    code:str
    compilable:str=""
    test_passed:str=""
    successful_trans:str=""

    @classmethod
    def from_dict(cls, data: dict) -> "TransformReuslt":
        return cls(
            project_name=data["project_name"],
            pair_id=data["pair_id"],
            src_id=data["src_id"] if "src_id" in data else "",
            code=data["code"],
            compilable=data["compilable"] if "compilable" in data else "",
            test_passed=data["test_passed"] if "test_passed" in data else "",
            successful_trans=data["successful_trans"] if "successful_trans" in data else ""
        )


@dataclass
class LLMConfig:
    do_sample: bool = True
    temperature: float = 0.6
    max_new_tokens: int = 100 * 1000
    
def save_pairs(pairs, output_path):
    with open(output_path, mode='w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['pair_id', 'src_dir', 'target_dir', 'result_dir'])  # 写入表头
        for pair in pairs:
            writer.writerow([pair.pair_id, pair.src_dir, pair.target_dir, pair.result_dir])

def load_pairs(input_path) -> list[TransformPairPack]:
    pairs = []
    with open(input_path, mode='r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            pairs.append(TransformPairPack(
                pair_id=row['pair_id'],
                src_dir=row['src_dir'],
                target_dir=row['target_dir'],
                result_dir=row['result_dir']
            ))
    return pairs

    
def create_transform_pair_pack(project_name, data:list[TransformPair], pack_author) -> list[TransformPairPack]:
    pairs_dict = {} # key:(src_author, target_author)

    if pack_author:
        for d in data:
            key = (d.src_author, d.target_author)
            if key not in pairs_dict:
                pairs_dict[key] = TransformPairPack(project_name,[d.pair_id], d.src_author, d.target_author, [d.src_id], d.target_ids)
            else:
                pairs_dict[key].src_ids.append(d.src_id)
                pairs_dict[key].pair_ids.append(d.pair_id)


        return list(pairs_dict.values())
    else:
        pack_pairs = []
        for d in data:
            tr = TransformPairPack(project_name,[d.pair_id], d.src_author, d.target_author, [d.src_id], d.target_ids)
            pack_pairs.append(tr)
        return pack_pairs

class DataCache:
    from author_tagger import AuthorIDMap

    data_dict:Dict[str, Data] = {}
    id_map = AuthorIDMap()
    id_map.load()

    @staticmethod
    def get_code_list(project_name, author, src_ids):
        code_list = []
        data = DataCache.get_data(project_name, author)
        data_list = data.get_data_list(author)
        data_dict = {d.id:d for d in data_list}
        code_list = [data_dict[id].get_code() for id in src_ids]

        if len(code_list) != len(src_ids):
            print("Error occurred when getting code")
        assert len(code_list) == len(src_ids)
        return code_list
    
    @staticmethod
    def get_data(project_name, author) -> Data:
        
        real_project_name = project_name
        if project_name == "across-project":
            real_project_name  = DataCache.id_map.get_author_project(author)
        
        if real_project_name in DataCache.data_dict:
            return DataCache.data_dict[real_project_name]

        data = Data(ProjectConfigs().get_project_by_name(real_project_name))
        DataCache.data_dict[real_project_name] = data
        return DataCache.data_dict[real_project_name]


def generate_prompt(pair:TransformPairPack):
    def get_wrapped_codes(project_name, author, ids):
        dir = os.path.join(create_codes_dir(project_name), author)
        code_list = DataCache.get_code_list(project_name, author, ids)
        code_list = [f"```java\n{code}\n```" for code in code_list]
        # filepaths = []
        # for id in ids:
        #     filepaths.append(os.path.join(dir, f"{id}.java"))
        # for file in filepaths:
        #         with open(file, 'r', encoding='utf8') as f:
        #             code = f.read()
        #         code_list.append(f"```java\n{code}\n```")
        return "\n".join(code_list)
    

    src_codes = get_wrapped_codes(pair.project_name, pair.get_src_author(), pair.src_ids)
    target_codes = get_wrapped_codes(pair.project_name, pair.get_target_author(), pair.target_ids)
    system_prompt = "You are a code style transformation tool."
    user_prompt = f"""
## Task Description
Given some code snippets written by Author A and some code snippets written by Author B,
your task is to perform style transformation on all code snippets of Author A based on the coding style demonstrated in Author B's codes.
Output all code snippets of Author A after transformation in the given output template, ensuring the output snippets strictly follow the original input order.

The transformation must meet the following constraints:
- The transformed code must be semantically equivalent to the original code. That is, its functionality must remain unchanged.
- The transformation must not introduce any changes that could affect the code’s interaction with the external environment, even if such interactions are not explicitly visible in the current snippet.

  This restriction includes, but is not limited to:
  - Renaming `public` or `protected` methods

The goal is:
- To rewrite all code snippets from Author A so that their style closely matches the style exhibited in Author B’s code, while fully preserving functionality.


## Input
### Code Snippets by Author A:
{src_codes}

### Style Reference — Code Snippets by Author B:
{target_codes}

## Output
```java
<Code snippet 1>
```
```java
<Code snippet 2>
```
...
"""
    return user_prompt, system_prompt


def create_transform_result(pair:TransformPairPack, model_output: str) -> list[TransformReuslt]:
    code_blocks = get_code_from_response(model_output, "java")

    ids = pair.pair_ids
    
    if len(code_blocks) != len(ids):
        print(f"Wrong result of ids:{ids}")
        return [TransformReuslt(pair.project_name, ",".join(ids), ",".join(pair.src_ids), "\n".join(code_blocks))]

    results = []
    for id_, code in zip(ids, code_blocks):
        src_id = pair.get_src_id(id_)
        results.append(TransformReuslt(pair.project_name, id_, src_id ,code))
    return results


class ResultManager:
    def __init__(self, output_file: str, flush_threshold: int = 1):
        """
        :param output_file: 所有结果统一写入的 jsonl 文件路径
        :param flush_threshold: 缓冲区中结果数量达到该值时写入文件
        """
        self.output_file = output_file
        self.flush_threshold = flush_threshold
        self.buffer: List[TransformReuslt] = []
        self.lock = threading.Lock()

        # 已有结果，用于去重或跳过
        self.result_dict: Dict[Tuple[str, str], TransformReuslt] = {} # key:(project_name, pair_id)
        if os.path.exists(self.output_file):
            self._load_existing_results()

        os.makedirs(os.path.dirname(self.output_file), exist_ok=True)

    def _load_existing_results(self):
        with open(self.output_file, "r", encoding="utf-8") as f:
            for line in f:
                data = json.loads(line.strip())
                result = TransformReuslt.from_dict(data)
                self.result_dict[(result.project_name, result.pair_id)] = result

    def get_existing_ids(self, pair: TransformPairPack) -> List[str]:
        with self.lock:
            return [pid for pid in pair.pair_ids if (pair.project_name, pid) in self.result_dict]

    def add_results(self, results: List[TransformReuslt]):
        with self.lock:
            self.buffer.extend(results)
            for r in results:
                self.result_dict[(r.project_name, r.pair_id)] = r
            if len(self.buffer) >= self.flush_threshold:
                self._flush_to_file()


    def _flush_to_file(self):
        with open(self.output_file, "a", encoding="utf-8") as f:
            for r in self.buffer:
                json.dump(asdict(r), f, ensure_ascii=False)
                f.write("\n")
        self.buffer.clear()

    def flush_all(self):
         with self.lock:
            if self.buffer:
                self._flush_to_file_locked()
            
    def update_all(self):
       with self.lock:
            with open(self.output_file, "w", encoding="utf-8") as f:
                for result in self.result_dict.values():
                    json.dump(asdict(result), f, ensure_ascii=False)
                    f.write("\n")
                
    def update(self, result):
        with self.lock:
            self.buffer.append(result)
            self.result_dict[(result.project_name, result.pair_id)] = result
            if len(self.buffer) >= self.flush_threshold:
                self._flush_to_file_locked()

                
    def get_all_results(self):
        with self.lock:
            return list(self.result_dict.values())

    
    def get_size(self):
        with self.lock:
            return len(self.result_dict)
    
    def get_project_classified_results(self):
        with self.lock:
            result = {}
            for r in self.result_dict.values():
                result.setdefault(r.project_name, []).append(r)
            return result

    
    def get_result(self,project_name, pair_id):
        with self.lock:
            return self.result_dict.get((project_name, pair_id), None)
        
    def get_result_by_id(self, pair_id) -> TransformReuslt:
        project_name = list(self.result_dict.keys())[0][0]
        return self.result_dict.get((project_name, pair_id), None)
    
    def remove(self, keys):
        with self.lock:
            self.result_dict = {k: v for k, v in self.result_dict.items() if k not in keys}


def get_code_from_response(text, language):
    if "```" not in text:
        return text.strip()

    if f"```{language}" in text:
        pattern = rf"```{language}(.*?)```"
    else:
        pattern = r"```(.*?)```"

    code_blocks = re.findall(pattern, text, re.DOTALL)
    return code_blocks



def run_llm(model_name, data:list[TransformPairPack], result_manager:ResultManager,  override=False):
    # gets API Key from environment variable OPENAI_API_KEY
    client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key="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",
    )
    
    llm_config = LLMConfig()
    for d in data:
        d.remove_ids(result_manager.get_existing_ids(d))
        if not d.pair_ids:
            continue
        
        user_prompt, system_prompt = generate_prompt(d)
        # print(user_prompt)
        
        completion = client.chat.completions.create(
            model=model_name,
            temperature=llm_config.temperature,
            max_tokens=llm_config.max_new_tokens,
            messages=[
                {"role": "user", "content": user_prompt},
                {"role": "system", "content": system_prompt}
            ]
        )
        response = completion.choices[0].message.content

        results = create_transform_result(d, response)
        result_manager.add_results(results)
        
    result_manager.flush_all()

       

def run_local_llm(model_path, data:list[TransformPairPack], result_manager:ResultManager, override=False):
    # tokenizer = AutoTokenizer.from_pretrained(model_path, trust_remote_code=True)
    # model = AutoModelForCausalLM.from_pretrained(
    #     model_path, trust_remote_code=True, torch_dtype=torch.bfloat16, device_map="auto"
    # )
    # llm_config = LLMConfig()
    # generation_config = GenerationConfig(
    #     do_sample=llm_config.do_sample,
    #     temperature=llm_config.temperature,
    #     max_new_tokens=llm_config.max_new_tokens,
    #     eos_token_id=tokenizer.eos_token_id,
    #     pad_token_id=tokenizer.pad_token_id,
    # )

    for d in data:
        d.remove_ids(result_manager.get_existing_ids(d))
        if not d.pair_ids:
            continue

        user_prompt, system_prompt = generate_prompt(d)
        messages = [{"role": "user", "content": user_prompt}]
        # print(user_prompt)
        
        # inputs = tokenizer.apply_chat_template(
        #     messages, add_generation_prompt=True, return_tensors="pt"
        # ).to(model.device)
        # # tokenizer.eos_token_id is the id of <|EOT|> token
        # outputs = model.generate(inputs, generation_config)
        response = "```code```"
        # response = tokenizer.decode(
        #     outputs[0][len(inputs[0]) :], skip_special_tokens=True
        # )
                
        results = create_transform_result(d, response)
        result_manager.add_results(results)
    result_manager.flush_all()
            
           
            

def save_code(pair:TransformPairPack):
    def do_save(output_dir, project_name, author, ids):
        code_list = DataCache.get_code_list(project_name, author, ids)
        os.makedirs(output_dir, exist_ok=True)
        codes_dir = create_codes_dir(project_name)

        for i in range(len(ids)):
            id = ids[i]
            path = os.path.join(output_dir, f"{id}.java")
            with open(path, "w", encoding="utf-8") as f:
                f.write(code_list[i])

        # for id in ids:
        #    src_path = os.path.join(codes_dir, author, f"{id}.java")
        #    dst_path = os.path.join(output_dir, f"{id}.java")
        #    shutil.copyfile(src_path, dst_path)


    do_save(pair.get_src_codes_dir(), pair.project_name, pair.src_author, pair.src_ids)
    do_save(pair.get_target_codes_dir(), pair.project_name, pair.target_author, pair.target_ids)
    
count = 0
    
def create_result_from_dir(pair:TransformPairPack):
    dir = pair.get_result_dir()
    results = []
    for idx, id in enumerate(pair.pair_ids):
        src_id = pair.get_src_id(id)
        filepath = os.path.join(dir, f"{pair.src_ids[idx]}.java")
        if os.path.exists(filepath):
            with open(filepath, 'r', encoding='utf8') as f:
                results.append(TransformReuslt(pair.project_name, id, src_id, f.read()))
        else:
            results.append(TransformReuslt(pair.project_name, id,src_id, ""))
    return results


def run_egsi(data:list[TransformPairPack], result_buffer:ResultManager):
    for d in data:
        # 保存transformation pair数据到目录
        save_code(d)
        jar_path = os.path.join("lib", r"style-core-1.0-SNAPSHOT.jar")
        command = ['java', '-cp', jar_path, "org.example.Main", "-src",d.get_src_codes_dir(), "-target", d.get_target_codes_dir(), "-d", d.get_result_dir()]

        print(' '.join(command))
        process = subprocess.Popen(
                command,
                bufsize=1,
                stdin=subprocess.PIPE,  # 允许向子进程发送数据
                stdout=subprocess.PIPE,  # 获取子进程的输出
                stderr=subprocess.PIPE,  # 获取错误输出
                text=True,
                encoding='utf-8'
        )
        
        stdout, stderr  = process.communicate()
        # print(stdout)
        
        
        result_buffer.add_results(create_result_from_dir(d))

    result_buffer.flush_all()


def run_codebuff(data:list[TransformPairPack], result_manager:ResultManager):    
    thread_id = threading.get_ident()
    

    for d in data:
        d.remove_ids(result_manager.get_existing_ids(d))
        if not d.pair_ids:
            continue

        # 封装target file
        target_codes = DataCache.get_code_list(d.project_name, d.target_author, d.target_ids)
        wrapped_target_dir = os.path.join(TMP_DATA, f"codebuff{thread_id}", "target")
        os.makedirs(wrapped_target_dir, exist_ok=True)
        for i in range(len(d.target_ids)):
            with open(os.path.join(wrapped_target_dir, f"{d.target_ids[i]}.java"), "w", encoding="utf-8") as f:
                f.write("class Main {\n" + target_codes[i] + "\n}\n")
            
        for pair_id in d.pair_ids:
            src_id = d.get_src_id(pair_id)
            src_code = DataCache.get_code_list(d.project_name, d.src_author, [src_id])[0]
            
            # 封装src_file
            wrapped_src_file = os.path.join(TMP_DATA, f"codebuff{thread_id}", f"{src_id}.java")
            os.makedirs(os.path.dirname(wrapped_src_file), exist_ok=True)
            with open(wrapped_src_file, "w", encoding="utf-8") as f:
                f.write("class Main {\n" + src_code + "\n}\n")

            command = [
            'java',
            '-jar',
            "lib/codebuff-1.5.1.jar",  # 替换为 JAR 文件路径
            '-g', 'org.antlr.codebuff.Java8',
            '-rule', 'compilationUnit',
            '-corpus', wrapped_target_dir,  # 替换为目标样本路径
            '-files', 'java',
            '-comment', 'LINE_COMMENT',
            wrapped_src_file  # 替换为源文件路径
        ]
            command_str = " ".join(command)
            print(f"Run:{command_str}")
            # 启动子进程执行command，然后收集标准输出
            process = subprocess.Popen(
                command,
                bufsize=1,
                stdin=subprocess.PIPE,  # 允许向子进程发送数据
                stdout=subprocess.PIPE,  # 获取子进程的输出
                stderr=subprocess.PIPE,  # 获取错误输出
                text=True  
            )
            stdout, stderr = process.communicate()
            output_code = "\n".join(stdout.splitlines()[1:])
            match = re.search(r'class\s+Main\s*{(.*)}\s*$', output_code, re.DOTALL)
            if match:
                code = match.group(1)
            else:
                code = output_code
            
            result = TransformReuslt(d.project_name, pair_id, src_id, code)
            result_manager.add_results([result])
            
            os.remove(wrapped_src_file)
        shutil.rmtree(wrapped_target_dir)

    result_manager.flush_all()
    
    shutil.rmtree(os.path.join(TMP_DATA, f"codebuff{thread_id}"))


def run_non(method, min_target_lines):
    configs = ProjectConfigs()
    for project in configs.projects:
        path = create_transformation_pair_path(project.name, min_target_lines)
        data = TransformationData.load_data(path)
        result_manager = ResultManager(create_transformation_result_jsonl_path(method, min_target_lines))
        for d in data:
            with open(os.path.join(create_codes_dir(project.name), d.src_author,  f"{d.src_id}.java"), 'r', encoding='utf8') as f:
                code=f.read()

                result = TransformReuslt(d.project_name, d.pair_id, d.src_id, code)
                result_manager.add_results([result])
        result_manager.flush_all()

def run(project_name, method, min_target_lines, pack=True):

    path = create_transformation_pair_path(project_name, min_target_lines)
    data = create_transform_pair_pack(project_name, TransformationData(project_name).load_data(path), pack)
    parallel_level = 10
    size = int(len(data) / parallel_level)
    batch_data = []
    for i in range(0, parallel_level):
        end = min(i * size + size, len(data))
        batch_data.append(data[i * size:end])
    if end < len(data):
        batch_data[-1].extend(data[end:])
    #多线程运行
    filepath = create_transformation_result_jsonl_path(os.path.basename(method).replace(":", "--"), min_target_lines)
    result_manager = ResultManager(filepath)
    try:

        if method.startswith("egsi"):
            with ThreadPoolExecutor(max_workers=parallel_level) as executor:
                futures = [executor.submit(run_egsi, batch, result_manager) for batch in batch_data]
                for future in futures:
                    future.result()
        elif method == "codebuff":
            with ThreadPoolExecutor(max_workers=parallel_level) as executor:
                futures = [executor.submit(run_codebuff, batch, result_manager) for batch in batch_data]
                for future in futures:
                    future.result()
        elif method == "deepseekcoder":
            run_local_llm(os.path.join(MODELS_DIR, "deepseek-coder-33b-instruct"), data, result_manager)
        else:
            run_llm(method, data, result_manager)
    except Exception as e:
        print(e)

def query_program_by_id(project_name):
    configs = ProjectConfigs()
    config = next((c for c in configs.projects if c.name == project_name), None)
            
    str = input()
    data = Data(config)
    id_author_dict = {d.id:d.get_dominant_author() for d in data.transformation_data}
    id_author_dict.update({d.id:d.get_dominant_author() for d in data.training_data})
    print(len(id_author_dict))
    while str != "q":
        filepath = os.path.join(create_codes_dir(project_name), id_author_dict[str], f"{str}.java")
        
        str = input()

def check_result_order(min_target_lines):
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
                original_code = DataCache.get_code_list(r.project_name, src_author, [r.src_id])
                parser.parse_code_str(original_code)
                original_signature = parser.get_method_signature()[0]

                parser.parse_code_str(r.code)
                result_signature = parser.get_method_signature()[0]
                
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
            
        res_manager.update_all()

                    
if __name__ == "__main__":
    # method = 'gpt-3.5-turbo'
    # method = 'deepseekcoder'
    # method = 'egsi'
    # method = 'codebuff'
    # method="deepseek/deepseek-r1-0528:free"
    # method = "openai/gpt-4.1"
    # method = "anthropic/claude-3.7-sonnet"

    # 判断参数数量
    import sys
    method = sys.argv[1]
    min_target_lines = int(sys.argv[2])
    if len(sys.argv) > 3:
        pack = sys.argv[3] == "True"
    else:
        pack = True    
        
    # for project in ProjectConfigs().projects:
    #     run(project.name, method, min_target_lines, pack)

    run("across-project", method, min_target_lines, pack)
