    public GrpcResultViewResolver registerView(Class modelClass, GrpcResultView view) {
        //TODO 检查model的type是否重复，避免复制代码带来的bug
        this.resultViewMap.put(modelClass, view);
        return this;
    }
