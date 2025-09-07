#!/bin/bash

# 用法：./run_with_time.sh "your command here"

CMD="python transform.py $1 $2"
echo "Run:$CMD"

# 记录开始时间（以秒为单位）
start_time=$(date +%s)

# 执行命令
eval "$CMD"

# 记录结束时间
end_time=$(date +%s)

# 计算并输出运行时间
elapsed=$((end_time - start_time))
echo "Command finished in $elapsed seconds."
