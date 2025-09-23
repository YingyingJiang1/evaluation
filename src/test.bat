@echo off
setlocal

REM 定义长度列表
set lengths=200 400 800 1000

REM 遍历每个长度
for %%L in (%lengths%) do (
    echo Running python test.py %%L ...
    python test.py %%L
    REM 等待当前命令完成后再执行下一个
    if errorlevel 1 (
        echo WARNING: test.py %%L failed!
    )
    echo Finished %%L
)

echo All tests finished.
pause
