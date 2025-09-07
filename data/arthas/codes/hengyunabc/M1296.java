    @Override
    final public void before(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args)
            throws Throwable {
        before(clazz.getClassLoader(), clazz, new ArthasMethod(clazz, methodName, methodDesc), target, args);
    }
