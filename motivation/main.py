from dataclasses import dataclass
from openai import OpenAI
import pandas as pd
import json
import os

def get_unique_task_ids(file_path):
    # 读取 Excel 文件
    df = pd.read_excel(file_path)

    # 检查是否存在 task_id 列
    if 'task_id' not in df.columns:
        raise ValueError(f"文件 {file_path} 中没有 'task_id' 列")

    # 获取唯一的 task_id（去重、去掉空值）
    unique_ids = df['task_id'].dropna().unique()

    # 转换成列表并排序
    return sorted(unique_ids)

import os
import json
import re

def extract_docs_and_code(json_file, output_dir="output", context_radius=100):
    """
    提取指定任务 ID 的代码片段（snippet）。
    - snippet 本身只写入函数签名（去掉函数体）
    - docstring 写在函数签名外部
    - snippet 上下各 n 行写入完整上下文

    参数:
        task_ids (list): 要提取的任务 ID 列表
        json_file (str): JSON 文件路径
        output_dir (str): 输出目录
        context_radius (int): snippet 上下各扩展的行数
    """
    os.makedirs(output_dir, exist_ok=True)

    with open(json_file, "r", encoding="utf-8") as f:
        data = json.load(f)

    if "RECORDS" not in data:
        raise ValueError("JSON 文件不包含 'RECORDS' 键")
    records = data["RECORDS"]

    id = 1
    classes = set()
    for obj in records:
        if not isinstance(obj, dict):
            continue
        obj_id = obj.get("_id")

        file_content = obj.get("file_content", "")
        lineno = int(obj.get("lineno", -1))
        end_lineno = int(obj.get("end_lineno", -1))
        code = obj.get("code", "")
        docstring = obj.get("docstring", "")

        lines = file_content.splitlines()

        # 计算上下文范围
        start = max(lineno - 1 - context_radius, 0)
        end = min(end_lineno + context_radius, len(lines))
        
        if (len(file_content.splitlines())) > 500:
            continue
        
        classname = obj.get("class_name")
        package = obj.get("package")
        if package + classname in classes:
            continue
        classes.add(package + classname)

        

        # snippet 上下文行
        # context_lines = lines[start:lineno-1]  # snippet 上方上下文
        # context_lines.append("")  # 空行分隔

        # 处理函数签名: 从 code 字段去掉函数体
        # code_lines = code.splitlines()
        # signature_lines = []
        # brace_count = 0
        # for line in code_lines:
        #     signature_lines.append(line)
        #     brace_count += line.count("{") - line.count("}")
        #     if brace_count > 0:
        #         break  # 只保留到函数体开始

        # # 写入 docstring 在函数签名外部
        # if docstring.strip():
        #     context_lines.extend(docstring.strip().splitlines())

        # context_lines.extend(signature_lines)
        # context_lines.append("}")  # 占位结束
        
        # context_lines.append(docstring)
        # context_lines.append(code)

        # context_lines += lines[end_lineno:end]  # snippet 下方上下文
        
        context_lines = []
        context_lines.append(file_content)

        snippet_with_context = "\n".join(context_lines)

        
        file_path = os.path.join(output_dir, f"{obj_id}.java")
        if (id == 1):
            print(obj_id)
        with open(file_path, "w", encoding="utf-8") as out:
            out.write(snippet_with_context)
            id += 1

            # print(f"已写入: {file_path}")
    print(len(classes))

def generate_prompt(prompt_path, input_folder: str) -> str:
    input_file = os.path.join(input_folder, "input.txt")
    full_context_file = os.path.join(input_folder, "full-context.java")
    with open(prompt_path, "r", encoding="utf-8") as f:
        prompt = f.read()
    with open(input_file, "r", encoding="utf-8") as f:
        prompt = prompt + f.read()
    with open(full_context_file, "r", encoding="utf-8") as f:
        prompt = prompt + f.read()
    return prompt

@dataclass
class LLMConfig:
    do_sample: bool = True
    temperature: float = 0.6
    max_new_tokens: int = 16 * 1000
    
def get_code_from_response(text, language):
    if "```" not in text:
        return text

    if f"```{language}" in text:
        pattern = rf"```{language}(.*?)```"
    else:
        pattern = r"```(.*?)```"

    code_blocks = re.findall(pattern, text, re.DOTALL)
    return ''.join(code_blocks)
   
def run_llm(model_name, input_folder):
    
    prompt = generate_prompt("prompt.txt", input_folder)
    if "deepseek" in model_name:
        completion = run_deepseek(prompt)
    else:
        # print(prompt)
        # exit(0)
        # gets API Key from environment variable OPENAI_API_KEY
        client = OpenAI(
        base_url="https://openrouter.ai/api/v1",
        api_key="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",
        )
        print(f"model:{model_name}")
        # models = client.models.list()
        # for m in models.data:
        #     print(m.id)
        
        llm_config = LLMConfig()
        completion = client.chat.completions.create(
                model=model_name,
                temperature=llm_config.temperature,
                max_tokens=llm_config.max_new_tokens,
                messages=[
                    {"role": "user", "content": prompt},
                    # {"role": "system", "content": "You are a programming assistant."}
                ]
            )
    print(completion)
    response = completion.choices[0].message.content
    print(response)
    code = get_code_from_response(response, "java")
    
    output_file = os.path.join(input_folder, f"{os.path.basename(model_name)}.java")
    with open(output_file, "w", encoding="utf-8") as f:
        f.write(code)

def run_deepseek(prompt):
    
    client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key="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",
    )
    completion = client.chat.completions.create(
    model="deepseek/deepseek-r1-0528",
    messages=[
        {
        "role": "user",
        "content": prompt
        }
    ]
    )
    # print(completion)
    return completion

if __name__ == "__main__":
    # generate_prompt("samples/sample3/input.java")
    # task_ids = get_unique_task_ids("GPT4_code_samples.xlsx")
    # extract_docs_and_code("CoderEval4Java.json", output_dir="samples")
    # extract_docs_and_code("CoderEval4Java.json", output_dir="selected-tasks")
    
    model = "deepseek/deepseek-r1-0528"
    model = "openai/gpt-3.5-turbo"
    # model = "openai/gpt-4.1"
    # model = "google/gemini-2.5-flash"
    # model = "x-ai/grok-4-fast"
    
    
    run_llm(model, "samples/sample1")
    