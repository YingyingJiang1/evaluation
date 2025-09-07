    @Override
    final public void afterThrowing(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args,
            Throwable throwable) throws Throwable {
        afterThrowing(clazz.getClassLoader(), clazz, new ArthasMethod(clazz, methodName, methodDesc), target, args,
                throwable);
    }
