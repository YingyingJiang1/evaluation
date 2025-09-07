    public static List<AdviceListener> queryTraceAdviceListeners(ClassLoader classLoader, String className,
            String owner, String methodName, String methodDesc) {
        classLoader = wrap(classLoader);
        className = className.replace('/', '.');
        ClassLoaderAdviceListenerManager manager = adviceListenerMap.get(classLoader);

        if (manager != null) {
            return manager.queryTraceAdviceListeners(className, owner, methodName, methodDesc);
        }

        return null;
    }
