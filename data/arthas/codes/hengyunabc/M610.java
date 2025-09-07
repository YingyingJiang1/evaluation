    @Override
    public void before(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target, Object[] args)
            throws Throwable {
        TraceEntity traceEntity = threadLocalTraceEntity(loader);
        traceEntity.tree.begin(clazz.getName(), method.getName(), -1, false);
        traceEntity.deep++;
        // 开始计算本次方法调用耗时
        threadLocalWatch.start();
    }
