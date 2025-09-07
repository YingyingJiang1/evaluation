    private void processClassSet(final CommandProcess process, final ClassLoaderVO classLoaderVO, Collection<Class<?>> classes, int pageSize, final RowAffect affect) {
        //分批输出classNames, Ctrl+C可以中断执行
        ResultUtils.processClassNames(classes, pageSize, new ResultUtils.PaginationHandler<List<String>>() {
            @Override
            public boolean handle(List<String> classNames, int segment) {
                process.appendResult(new ClassLoaderModel().setClassSet(new ClassSetVO(classLoaderVO, classNames, segment)));
                affect.rCnt(classNames.size());
                return !checkInterrupted(process);
            }
        });
    }
