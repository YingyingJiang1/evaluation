@echo off
setlocal ENABLEDELAYEDEXPANSION

:: 设置 DOCKER_DIR
set "DOCKER_DIR=..\docker"

:: 函数：构建指定项目
:build_project
set "project_name=%~1"
set "dockerfile_path=%DOCKER_DIR%\%project_name%.Dockerfile"
set "image_name=%project_name%-image"

if not exist "%dockerfile_path%" (
    echo 找不到 Dockerfile: %dockerfile_path%
    exit /b 1
)

echo 正在构建镜像 %image_name%...
docker build -f "%dockerfile_path%" -t "%image_name%" .
if %ERRORLEVEL% EQU 0 (
    echo 构建成功：%image_name%
) else (
    echo 构建失败：%image_name%
)
exit /b

:: 主逻辑
if not "%~1"=="" (
    call :build_project %1%
) else (
    echo 开始批量构建所有 Dockerfile...
    for %%F in (%DOCKER_DIR%\*.Dockerfile) do (
        set "filename=%%~nxF"
        set "project_name=!filename:.Dockerfile=!"
        call :build_project !project_name!
    )
)

endlocal
