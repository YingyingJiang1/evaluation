    @Override
    public void afterReturning(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target, Object[] args,
                               Object returnObject) throws Throwable {
        //取出入参时的 args，因为在函数执行过程中 args可能被修改
        args = (Object[]) argsRef.get().pop();
        afterFinishing(Advice.newForAfterReturning(loader, clazz, method, target, args, returnObject));
    }
