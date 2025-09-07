import os
from openai import OpenAI
from buffer import BufferedWriter
from prompt import create_single_file_prompt, create_task_speicific_prompt
import json
import requests
import json

def create_chat_content(model, prompt):
    if "gemini" in model or "gpt" in model:
        return [
            {
            "type": "text",
            "text": prompt
            }
        ]  
        
    else:
        return prompt

def run_llm(model, batch_prompt, output_file, **kwargs):
    client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key="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",
    )
    
    buffer = BufferedWriter(output_file)
    for key in batch_prompt:
        try:
            completion = client.chat.completions.create(
        model=model,
        temperature=kwargs["temperature"],
        max_tokens=kwargs["max_tokens"],
        messages=[
        {
        "role": "user",
        "content": create_chat_content(model, batch_prompt[key])
        }
    ]
    )   
            print(batch_prompt[key])
            # print(completion)
            response = completion.choices[0].message.content
            buffer.write({"key": key, "response": response})
            # print(response)
        except Exception as e:
            print("Error: ", e)
        
    buffer.close()
    

    
def ceate_batch_prompt(dir, style_aspects="detail"):
    batch_prompt = {}
    for file in os.listdir(dir):
        if "format" not in file or not file.endswith(".java"):
            continue
        prompt = create_single_file_prompt(os.path.join(dir, file), style_ascpects=style_aspects)
        batch_prompt[file] = prompt
    return batch_prompt

def create_prompt_from_json(dir):
    with open(os.path.join(dir, 'inconsistencies.json'), encoding='utf-8') as f:
        data = json.load(f)
    batch_prompt = {}
    for d in data:
        file = os.path.join(dir, d['file'])
        inconsistencies = d['inconsistencies']
        prompt = create_task_speicific_prompt(inconsistencies, file)
        batch_prompt[file] = prompt
        print(f"{file}\n{inconsistencies}")
    return batch_prompt




def write_response_to_file(response_file, output_dir):
    
    with open(response_file, 'r', encoding='utf-8') as f:
        response_json_list = json.load(f)
    for response_json in response_json_list:
        filename = os.path.basename(response_json["key"])
        content = response_json["response"]

        os.makedirs(output_dir, exist_ok=True)  # 如果输出目录不存在，创建它

        file_path = os.path.join(output_dir, filename)
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Wrote file: {file_path}")
    
    


def run_llm_by_self_server(model, batch_prompt, output_file, **kwargs):
    buffer = BufferedWriter(output_file)
    for key in batch_prompt:
        prompt = batch_prompt[key]
        try:
            response = requests.post(
    url="http://210.28.134.91:8800/deepseekcoder",
    headers={
        "Authorization": "Bearer <OPENROUTER_API_KEY>",
        "Content-Type": "application/json",
    },
    data=json.dumps({
        "question": prompt
    }
    )
    )
            print(response.json())
            buffer.write({"key": key, "response": response.json()["response"]})
            # print(response)
        except Exception as e:
            print("Error: ", e)
        
    buffer.close()
    
    
    



if __name__ == "__main__":
    project_dir = "../example/Stirling-PDF"
    style_aspects = "detail"
    batch_prompt = ceate_batch_prompt(os.path.join(project_dir, 'srcs', "manual"), style_aspects=style_aspects)
    # batch_prompt = create_prompt_from_json(os.path.join(project_dir, 'srcs', "manual"))
    # model_name = "google/gemini-2.5-pro-exp-03-25:free"
    model_name = "google/gemini-2.5-pro-preview-03-25"
    # model_name = "anthropic/claude-3.5-sonnet"
    # model_name = "x-ai/grok-3-beta"
    # model_name = "deepseek/deepseek-v3-base:free"
    # model_name = "openai/gpt-4o-2024-11-20"
    prefix = model_name.split("/")[1].replace(":", "_")
    dir_name = "output"
    output_file = os.path.join(project_dir, dir_name, prefix + "-output" + ".json")
    run_llm(model_name, batch_prompt, output_file, temperature=0.6, max_tokens=1024 * 10)
    
    write_response_to_file(output_file, os.path.join(project_dir, dir_name, os.path.basename(output_file).replace(".json", "")))
    
