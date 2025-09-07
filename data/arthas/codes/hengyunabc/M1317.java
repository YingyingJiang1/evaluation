    public Object invoke(Object target, Object... args)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        initMethod();
        if (method != null) {
            return method.invoke(target, args);
        } else if (this.constructor != null) {
            return constructor.newInstance(args);
        }
        return null;
    }
