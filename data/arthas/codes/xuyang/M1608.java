    public static void registerTraceAdviceListener(ClassLoader classLoader, String className, String owner,
            String methodName, String methodDesc, AdviceListener listener) {
        classLoader = wrap(classLoader);
        className = className.replace('/', '.');

        ClassLoaderAdviceListenerManager manager = adviceListenerMap.get(classLoader);

        if (manager == null) {
            manager = new ClassLoaderAdviceListenerManager();
            adviceListenerMap.put(classLoader, manager);
        }
        manager.registerTraceAdviceListener(className, owner, methodName, methodDesc, listener);
    }
