    @Override
    public void afterThrowing(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target, Object[] args,
                              Throwable throwable) {
        //取出入参时的 args，因为在函数执行过程中 args可能被修改
        args = (Object[]) argsRef.get().pop();
        afterFinishing(Advice.newForAfterThrowing(loader, clazz, method, target, args, throwable));
    }
