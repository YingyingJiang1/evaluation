    @Override
    public void afterReturning(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target, Object[] args,
                               Object returnObject) throws Throwable {
        threadLocalTraceEntity(loader).tree.end();
        final Advice advice = Advice.newForAfterReturning(loader, clazz, method, target, args, returnObject);
        finishing(loader, advice);
    }
