    @Override
    public void afterThrowing(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target, Object[] args,
                              Throwable throwable) throws Throwable {
        scriptListener.afterThrowing(output, Advice.newForAfterThrowing(loader, clazz, method, target, args, throwable));
    }
