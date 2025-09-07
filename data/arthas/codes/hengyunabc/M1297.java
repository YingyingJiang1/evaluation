    @Override
    final public void afterReturning(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args,
            Object returnObject) throws Throwable {
        afterReturning(clazz.getClassLoader(), clazz, new ArthasMethod(clazz, methodName, methodDesc), target, args,
                returnObject);
    }
