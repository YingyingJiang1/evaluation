#!/bin/bash

regex_pattern=$1

train_forsee() {

    # echo "Dataset dir: $dataset_dir"
    # echo "Cache dir:   $cache_dir"

    max_concurrent_per_gpu=3
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

    cd lib/Forsee

    local dataset_dir="$(pwd)/dataset"
    local cache_dir="$(pwd)/caches"

    # 遍历 dataset_dir 下所有子目录
    for dir in "$dataset_dir"/*; do
        if [ -d "$dir" ] && [[ $(basename "$dir") =~ $regex_pattern ]]; then
            dir_name=$(basename "$dir")

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
            echo "Using GPU $gpu_id (current usage: ${gpu_usage[$gpu_id]})"

            # ===== 启动训练任务，并在完成后减少 GPU 计数 =====
            (
                # 在子 shell 中运行训练任务
                # 如果缓存目录已存在，跳过
                if [ -d "$cache_dir/$dir_name" ]; then
                    echo "Skipping training $dir_name (cache exists)"
                else
                    echo "Run:CUDA_VISIBLE_DEVICES=$gpu_id python main.py training_forsee dataset/$dir_name java40 --k=10 --device=gpu"
                    CUDA_VISIBLE_DEVICES=$gpu_id python main.py training_forsee dataset/$dir_name java40 --k=10 --device=gpu
                fi

                eval_dataset_name="${dir_name}-eval"
                output_path="../../../tmp-data/forsee/classify/${eval_dataset_name}-result.jsonl"

                if [ ! -f "$output_path" ]; then
                    echo "Run:CUDA_VISIBLE_DEVICES=$gpu_id python classify.py classify_dataset caches/$dir_name dataset/${eval_dataset_name} caches/${eval_dataset_name} $output_path --device=gpu"
                    CUDA_VISIBLE_DEVICES=$gpu_id python classify.py classify_dataset caches/$dir_name dataset/${eval_dataset_name} caches/${eval_dataset_name} $output_path --device=gpu
                fi
                # 任务完成后减少 GPU 使用计数
                # gpu_usage[$gpu_id]=$(( ${gpu_usage[$gpu_id]} - 1 ))
                # echo "✓ Task on GPU $gpu_id completed. Updated usage: ${gpu_usage[$gpu_id]}"
            ) &  # 后台运行
        fi
    done

        # 清理正确率不满足要求的分类器
        # echo "Removing low accuracy classifiers for pattern: $regex_pattern"
        # cd ../..
        # python forsee.py remove_low_accuracy_classifiers --accuracy_threshold=0.8

}

train_forsee