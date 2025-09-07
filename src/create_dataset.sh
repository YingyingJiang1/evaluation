#!/bin/bash

target_code_lines_thresh="${1:-200}"
DO_TRAIN="${2:-false}"


echo "Generating forsee training dataset and testing dataset..."
readarray -t dir_regex < <(
  python -c "
import subprocess
project_names = subprocess.run(
    ['python','dataset.py','create_binary_classification_data'],
    stdout=subprocess.PIPE, text=True
).stdout.strip().splitlines()
for name in project_names:
    print(f'^' + name + '[0-9]+$')"
)


train_forsee() {

    # echo "Dataset dir: $dataset_dir"
    # echo "Cache dir:   $cache_dir"

    max_concurrent_per_gpu=10
        # 获取 GPU 数量
    gpu_count=$(nvidia-smi --query-gpu=index --format=csv,noheader,nounits | wc -l)

    # 初始化 GPU 使用计数数组
    declare -a gpu_usage
    for ((i=0; i<gpu_count; i++)); do
        gpu_usage[i]=0
    done

    # 获取 memory.used 小于阈值的 GPU 列表
    get_free_gpus(){ 
    nvidia-smi --query-gpu=index,memory.used --format=csv,noheader,nounits |     awk -v threshold="1000" '$2+0 < threshold {print $1}'| tr -d ','
    }


    # 找到一个可用的 GPU（空闲且未超过最大使用数）
    find_available_gpu() {
        local free_gpus=($(get_free_gpus))
        for gpu in "${free_gpus[@]}"; do
            if [ "${gpu_usage[$gpu]}" -lt "$max_concurrent_per_gpu" ]; then
                echo "$gpu"
                return 0
            fi
        done

        echo ""
        return 1
    }

    # 遍历所有正则表达式
    for regex_pattern in "${dir_regex[@]}"; do
        cd lib/Forsee

        local dataset_dir="$(pwd)/dataset"
        local cache_dir="$(pwd)/caches"

        # 遍历 dataset_dir 下所有子目录
        for dir in "$dataset_dir"/*; do
            if [ -d "$dir" ] && [[ $(basename "$dir") =~ $regex_pattern ]]; then
                dir_name=$(basename "$dir")

                # 如果缓存目录已存在，跳过
                if [ -d "$cache_dir/$dir_name" ]; then
                    echo "Skipping $dir_name (cache exists)"
                    continue
                fi

                gpu_id=""
                retry_count=0
                MAX_GPU_RETRIES=3
                GPU_RETRY_DELAY=120


                # 循环尝试获取可用 GPU
                while [[ -z "$gpu_id" && $retry_count -lt $MAX_GPU_RETRIES ]]; do
                    gpu_id=$(find_available_gpu)  # 调用你的 GPU 查找函数

                    if [[ -z "$gpu_id" ]]; then
                        retry_count=$((retry_count + 1))
                        echo "⚠️ No available GPU for $dir_name. Retrying in $GPU_RETRY_DELAY seconds... ($retry_count/$MAX_GPU_RETRIES)"
                        sleep "$GPU_RETRY_DELAY"
                    fi
                done

                # 检查是否找到 GPU
                if [[ -z "$gpu_id" ]]; then
                    echo "❌ Error: Failed to find GPU for $dir_name after $MAX_GPU_RETRIES retries. Aborted."
                    return 1
                fi

                # 记录 GPU 使用情况
                gpu_usage[$gpu_id]=$(( ${gpu_usage[$gpu_id]:-0} + 1 ))
                echo "✔️ Training $dir_name on GPU $gpu_id (current usage: ${gpu_usage[$gpu_id]})"

                # ===== 启动训练任务，并在完成后减少 GPU 计数 =====
                (
                    # 在子 shell 中运行训练任务
                    CUDA_VISIBLE_DEVICES=$gpu_id python main.py training_forsee dataset/$dir_name java40 --k=10 --device=gpu

                    eval_dataset_name="${dir_name}-eval"
                    CUDA_VISIBLE_DEVICES=$gpu_id python classify.py classify_dataset caches/$dir_name dataset/${eval_dataset_name} caches/${eval_dataset_name} ../../../tmp-data/forsee/classify/${eval_dataset_name}-result.jsonl --device=gpu

                    # 任务完成后减少 GPU 使用计数
                    gpu_usage[$gpu_id]=$(( ${gpu_usage[$gpu_id]} - 1 ))
                    echo "✓ Task on GPU $gpu_id completed. Updated usage: ${gpu_usage[$gpu_id]}"
                ) &  # 后台运行
            fi
        done
        wait

        # 清理正确率不满足要求的分类器
        # echo "Removing low accuracy classifiers for pattern: $regex_pattern"
        # cd ../..
        # python forsee.py remove_low_accuracy_classifiers --accuracy_threshold=0.8
    done

}

if $DO_TRAIN; then
    echo "Training forsee classifiers..."
    # cd lib/Forsee
    # train_forsee
    # python forsee.py remove_low_accuracy_classifiers --accuracy_threshold=0.8
fi

echo "Creating transform pairs..."
python dataset.py create_transform_pairs --min_target_code_lines=$target_code_lines_thresh

echo "Saving the authors' codes to seperated files..."
python dataset.py save_codes
