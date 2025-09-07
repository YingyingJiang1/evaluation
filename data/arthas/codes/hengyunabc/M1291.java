    public static void registerAdviceListener(ClassLoader classLoader, String className, String methodName,
            String methodDesc, AdviceListener listener) {
        classLoader = wrap(classLoader);
        className = className.replace('/', '.');

        ClassLoaderAdviceListenerManager manager = adviceListenerMap.get(classLoader);

        if (manager == null) {
            manager = new ClassLoaderAdviceListenerManager();
            adviceListenerMap.put(classLoader, manager);
        }
        manager.registerAdviceListener(className, methodName, methodDesc, listener);
    }
