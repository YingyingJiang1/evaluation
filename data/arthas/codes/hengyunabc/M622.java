    @Override
    public void before(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target, Object[] args)
            throws Throwable {
        argsRef.get().push(args);
        threadLocalWatch.start();
    }
