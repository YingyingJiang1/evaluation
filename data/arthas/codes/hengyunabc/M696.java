    @Override
    public void before(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target, Object[] args)
            throws Throwable {
        scriptListener.before(output, Advice.newForBefore(loader, clazz, method, target, args));
    }
