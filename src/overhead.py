import time
import csv
import threading
import psutil
import time
import numpy as np
import subprocess
import random
import json
import os
import statistics
from datetime import datetime
import re
import shutil
import pandas as pd

from dataset import TransformationData, TransformPair
from utils import *
from transform import (
    create_transform_pair_pack,
    ResultManager,
    create_transform_result, run_codebuff,
    save_code,create_result_from_dir,LLMConfig, generate_prompt,

    DataCache, TransformReuslt
)

MONITOR = False

# ============ 实验配置 ============
METHODS = [
            # "egsi",
            # "egsi-newformat",
            # "egsi-lexical",
            # "egsi-syntactic",
            # "egsi-format",
            # "egsi-naming",
            # "egsi-structure",
            # "codebuff",
            "deepseek-r1-0528",
            # "gpt-4.1",
            # "claude-3.7-sonnet"
        ]
LINE_SIZES = [200, 400]
P = 0.05   # 采样百分比
K = 3    # 重复次数
project_name = "across-project"

OVERHEAD_RESULT_DIR_200 = os.path.join(EVAL_DIR, "overhead", "200")
OVERHEAD_RESULT_DIR_400 = os.path.join(EVAL_DIR, "overhead", "400")
OVERHEAD_RESULT_DIR_800 = os.path.join(EVAL_DIR, "overhead", "800")

OVERHEAD_RESULT_DIR_1000 = os.path.join(EVAL_DIR, "overhead", "1000")



def create_output_file(line_size, method):
    return os.path.join(EVAL_DIR, "overhead", str(line_size), f"overhead_{method}.csv")


def create_overhead_transformation_result_jsonl_path(method, min_target_code_lines):
    return os.path.join(EVAL_DIR, "overhead", "data", f"{min_target_code_lines}_{method}-result.jsonl")

# ============ 模拟接口调用（需替换为你的风格转换函数） ============
def run_style_transfer(method, line_size, source_code):
    """
    在这里替换为实际的调用逻辑。
    例如：
        result = openrouter_call(model=method, prompt=source_code)
    这里只用 sleep 模拟耗时。
    """
    simulated_time = random.uniform(0.5, 2.0)  # 模拟0.5~2秒的执行时间
    time.sleep(simulated_time)
    return "transformed_code", 500, 700, 0.01  # output, tokens_in, tokens_out, tokens_total


# ============ 资源监控函数 ============
def get_gpu_usage():
    try:
        output = subprocess.check_output(
            ["nvidia-smi", "--query-gpu=utilization.gpu,memory.used", "--format=csv,noheader,nounits"]
        ).decode("utf-8").strip().split("\n")[0]
        gpu_util, mem_used = map(float, output.split(","))
        return gpu_util, mem_used
    except Exception:
        return 0.0, 0.0


# ============ 写入CSV ============
def write_csv_header(file):
    dir = os.path.dirname(file)
    os.makedirs(dir, exist_ok=True)
    with open(file, "a", newline="") as f:
        writer = csv.writer(f)
        writer.writerow([
            "timestamp", "method", "line_size", "run_id",
            "t_total_s", "throughput", "cpu_avg_time(s)", "cpu_peak_time(s)", "ram_avg", "ram_peak",
            "tokens_in", "tokens_out", "tokens_total"
        ])


def append_csv_row(file, row):
    with open(file, "a", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(row)
        
        
def load_pairs(line_size) -> list[TransformPair]:
    pair_path = create_transformation_pair_path("across-project", line_size)
    pairs = TransformationData.load_data(pair_path)
    return pairs

def monitor_process(pid, cpu_delta_samples, ram_samples, sample_interval=1):
    """
    每个采样周期记录CPU时间增量（每秒消耗多少CPU秒）
    """
    try:
        proc = psutil.Process(pid)
        prev_times = proc.cpu_times()
        prev_total = prev_times.user + prev_times.system

        while proc.is_running():
            time.sleep(sample_interval)

            curr_times = proc.cpu_times()
            curr_total = curr_times.user + curr_times.system
            delta_cpu_time = curr_total - prev_total

            cpu_delta_samples.append(delta_cpu_time)
            ram_samples.append(proc.memory_percent())

            prev_total = curr_total

    except psutil.NoSuchProcess:
        pass

def run_egsi(data, result_buffer:ResultManager):
    cpu_samples, ram_samples = [], []
    exe_times = []
    
    # no input was found, skip style processing
    jar_path = os.path.join("lib", r"style-core-1.0-SNAPSHOT.jar")
    command = ['java', '-cp', jar_path, "org.example.Main", "-src","", "-target", "", "-d", ""]

    start_time = time.time()
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
        
    end_time = time.time()
    process_time = end_time - start_time
    print(process_time)
    
    for d in data:
        # 保存transformation pair数据到目录
        # save_code(d)
        jar_path = os.path.join("lib", r"style-core-1.0-SNAPSHOT.jar")
        command = ['java', '-cp', jar_path, "org.example.Main", "-src",d.get_src_codes_dir(), "-target", d.get_target_codes_dir(), "-d", d.get_result_dir()]

        print(' '.join(command))
        start_time = time.time()
        process = subprocess.Popen(
                command,
                bufsize=1,
                stdin=subprocess.PIPE,  # 允许向子进程发送数据
                stdout=subprocess.PIPE,  # 获取子进程的输出
                stderr=subprocess.PIPE,  # 获取错误输出
                text=True,
                encoding='utf-8'
        )
        
        if MONITOR:
            monitor_thread = threading.Thread(
                target=monitor_process, args=(process.pid, cpu_samples, ram_samples), daemon=True
            )
            monitor_thread.start()
        
        stdout, stderr  = process.communicate()
        
        end_time = time.time()
        duration = end_time - start_time - process_time
        exe_times.append(duration)
        # print(f"子进程执行时间: {duration:.2f} 秒")
        # print(stdout)
        
        if MONITOR:
            monitor_thread.join(timeout=1)
        
        result_buffer.add_results(create_result_from_dir(d))
        # print(f"cpu sampels: {cpu_samples}\n ram samples: {ram_samples}")

    cpu_avg = round(statistics.mean(cpu_samples), 2) if cpu_samples else 0.0
    cpu_peak = round(max(cpu_samples), 2) if cpu_samples else 0.0
    ram_avg = round(statistics.mean(ram_samples), 2) if ram_samples else 0.0
    ram_peak = round(max(ram_samples), 2) if ram_samples else 0.0
    
    return sum(exe_times), np.mean(exe_times), cpu_avg, cpu_peak, ram_avg, ram_peak


def run_codebuff(data, result_manager:ResultManager):    
    thread_id = threading.get_ident()
    cpu_samples, ram_samples = [], []
    exe_times = []
    
    start_time = time.time()
    command = [
            'java',
            '-jar',
            "lib/codebuff-1.5.1.jar",  # 替换为 JAR 文件路径
            '-g', 'org.antlr.codebuff.Java8',
            '-rule', 'compilationUnit',
            '-corpus', "",  # 替换为目标样本路径
            '-files', 'java',
            '-comment', 'LINE_COMMENT',
            ""  # 替换为源文件路径
    ]
    process = subprocess.Popen(
                command,
                bufsize=1,
                stdin=subprocess.PIPE,  # 允许向子进程发送数据
                stdout=subprocess.PIPE,  # 获取子进程的输出
                stderr=subprocess.PIPE,  # 获取错误输出
                text=True  
            )
    stdout, stderr = process.communicate()
    end_time = time.time()
    process_time = end_time - start_time
    

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
            start_time = time.time()
            process = subprocess.Popen(
                command,
                bufsize=1,
                stdin=subprocess.PIPE,  # 允许向子进程发送数据
                stdout=subprocess.PIPE,  # 获取子进程的输出
                stderr=subprocess.PIPE,  # 获取错误输出
                text=True  
            )
            
            if MONITOR:
                monitor_thread = threading.Thread(
                target=monitor_process, args=(process.pid, cpu_samples, ram_samples), daemon=True
            )
                monitor_thread.start()
            
            stdout, stderr = process.communicate()
            
            end_time = time.time()
            duration = end_time - start_time - process_time
            exe_times.append(duration)
            
            if MONITOR:
                monitor_thread.join(timeout=1)
            
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

    shutil.rmtree(os.path.join(TMP_DATA, f"codebuff{thread_id}"))
    cpu_avg = round(statistics.mean(cpu_samples), 2) if cpu_samples else 0.0
    cpu_peak = round(max(cpu_samples), 2) if cpu_samples else 0.0
    ram_avg = round(statistics.mean(ram_samples), 2) if ram_samples else 0.0
    ram_peak = round(max(ram_samples), 2) if ram_samples else 0.0
    return sum(exe_times), np.mean(exe_times), cpu_avg, cpu_peak, ram_avg, ram_peak


def run_llm(model_name, data, result_manager:ResultManager,  override=False):
    input_tokens, output_tokens, total_tokens = 0, 0, 0
    # gets API Key from environment variable OPENAI_API_KEY
    client = OpenAI(
    base_url="https://api.deepseek.com",
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
            model="deepseek-chat",
            temperature=llm_config.temperature,
            max_tokens=llm_config.max_new_tokens,
            messages=[
                {"role": "user", "content": user_prompt},
                {"role": "system", "content": system_prompt}
            ]
        )
        usage = completion.usage
        prompt_toks = usage.prompt_tokens
        completion_toks = usage.completion_tokens
        total_toks = usage.total_tokens

        input_tokens += prompt_toks
        output_tokens += completion_toks
        total_tokens += total_toks
        
        
        response = completion.choices[0].message.content

        results = create_transform_result(d, response)
        result_manager.add_results(results)
        print(response)
    
    return input_tokens, output_tokens, total_tokens


def execute_style_transfer(pairs:list[TransformPair], method, line_size, result_manager):
    data = create_transform_pair_pack(project_name, pairs, False)
    for d in data:
        save_code(d)
    infos = None
    while result_manager.get_size() < len(data):
        print("starting run...")
        try:
            if method.startswith("egsi"):
                infos = run_egsi(data, result_manager)
            elif method == "codebuff":
                infos = run_codebuff(data, result_manager)
            else:
                infos = run_llm(method, data, result_manager)
        except Exception as e:
            print(e)
            
    return infos

def monitor_resources(process, interval, stop_event, cpu_samples, ram_samples):
    """后台线程：定期采样 CPU 和 RAM"""
    while not stop_event.is_set():
        cpu_samples.append(psutil.cpu_percent(interval=None))
        ram_samples.append(process.memory_info().rss / (1024 * 1024))  # MB
        time.sleep(interval)
        
    
def test_overhead():
    random.seed(42)
    process = psutil.Process()
    tok_in, tok_out, tok_tol = 0, 0, 0
    infos = None
    for line_size in LINE_SIZES:
        for run_id in range(1, K + 1):
            print(f"Round {run_id}/{K}")
            pairs = load_pairs(line_size)
            M = int(len(pairs) * P)
            sampled_pairs = random.sample(pairs, M)
            now_time = datetime.now().isoformat(timespec='seconds')
            ids = [p.pair_id for p in sampled_pairs]
            print(ids)

            
            for method in METHODS:
                output_file = create_output_file(line_size, method)
                write_csv_header(output_file)
                
                filepath = create_overhead_transformation_result_jsonl_path(os.path.basename(method).replace(":", "--"), line_size)
                filepath = filepath.replace(".jsonl", str(run_id) + ".jsonl")
                
                result_manager = ResultManager(filepath, 1000) # 避免自动flush
                
                t0 = time.perf_counter()
                infos = execute_style_transfer(sampled_pairs, method, line_size, result_manager)
                t1 = time.perf_counter()
                t_total = t1 - t0 # second
                
                result_manager.update_all()
                if infos is not None:
                    if len(infos) == 3:
                        tok_in, tok_out, tok_tol = infos        
                        t_mean = t_total / M
                    else:
                        t_total, t_mean, cpu_avg, cpu_peak, ram_avg, ram_peak = infos
                
                """
                header: "timestamp", "method", "line_size", "run_id",
            "t_total_s", "throughput", "cpu_avg", "cpu_peak "ram_avg", "ram_peak",
            "tokens_in", "tokens_out", "tokens_total"
                """

                row = [
                        now_time,
                        method, line_size, run_id,
                        round(t_total, 2), round(t_mean, 2),
                        cpu_avg, cpu_peak, ram_avg, ram_peak,
                        tok_in, tok_out, tok_tol,
                    ]
                append_csv_row(output_file, row)
                
            

def calc_overhead(input_folder, output_file="overhead.csv"):
    res = []
    for f in os.listdir(input_folder):
        if f.endswith(".csv"):
            df = pd.read_csv(os.path.join(input_folder, f))
            start = df.columns.get_loc("t_total_s")
            means = df.iloc[:, start:].mean().to_dict()
            res.append({"filename": f, **means})
    pd.DataFrame(res).to_csv(output_file, index=False)
    print(f"✅ 结果已保存到 {output_file}")
    
if __name__ == "__main__":
    # test_overhead()
    
    # calc_overhead(OVERHEAD_RESULT_DIR_200,  os.path.join(OVERHEAD_RESULT_DIR_200, "overhead.csv"))
    # calc_overhead(OVERHEAD_RESULT_DIR_1000,  os.path.join(OVERHEAD_RESULT_DIR_1000, "overhead.csv"))
    # calc_overhead(OVERHEAD_RESULT_DIR_400,  os.path.join(OVERHEAD_RESULT_DIR_400, "overhead.csv"))
    calc_overhead(OVERHEAD_RESULT_DIR_800,  os.path.join(OVERHEAD_RESULT_DIR_800, "overhead.csv"))
