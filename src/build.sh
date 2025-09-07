#!/bin/bash

# 假设你当前目录是 scripts/，项目根目录在 scripts/../
DOCKER_DIR="../docker"
PROJECTS_DIR="../projects"


# 构建指定项目
build_project() {
    local project_name=$(echo "$1" | tr '[:upper:]' '[:lower:]') 
    local dockerfile_path="$DOCKER_DIR/${project_name}.Dockerfile"
    local image_name="${project_name}-image"

    if [ ! -f "$dockerfile_path" ]; then
        echo "❌ 找不到 Dockerfile: $dockerfile_path"
        return 1
    fi

    echo "🔨 正在构建镜像 $image_name..."
    PROJECT_ROOT_DIR="${PROJECTS_DIR}/$1"
    docker build \
        -f "$dockerfile_path" \
        -t "$image_name" \
        --build-arg PROJECT_NAME="$project_name" \
        --build-arg https_proxy=http://114.212.80.7:21087 \
        --build-arg http_proxy=http://114.212.80.7:21087 \
        "$PROJECT_ROOT_DIR"  # 把项目根目录作为上下文传入

    if [ $? -eq 0 ]; then
        echo "✅ 构建成功：$image_name"
    else
        echo "❌ 构建失败：$image_name"
    fi
    echo "-------------------------"
}

# 主逻辑
if [ $# -ge 1 ]; then
    if [ "$1" == "*" ]; then
        echo "🔁 开始批量构建所有 Dockerfile..."
        for file in "$DOCKER_DIR"/*.Dockerfile; do
            filename=$(basename -- "$file")
            project_name="${filename%.Dockerfile}"
            build_project "$project_name"
        done
    else
        build_project "$1"
    fi
else
    echo "ℹ️ 未指定参数，默认批量构建所有 Dockerfile..."
    for file in "$DOCKER_DIR"/*.Dockerfile; do
        filename=$(basename -- "$file")
        project_name="${filename%.Dockerfile}"
        build_project "$project_name"
    done
fi
