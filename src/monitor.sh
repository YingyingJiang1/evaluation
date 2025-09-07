#!/bin/bash

CMD="python transform.py deepseek/deepseek-r1-0528:free $1"

# 每隔30秒检查一次
while true; do
    if ! pgrep -f "$CMD" > /dev/null; then
        echo "[$(date)] Not running. Restarting...$CMD"
        nohup $CMD > /dev/null 2>&1 &
    fi
    sleep 60
done
