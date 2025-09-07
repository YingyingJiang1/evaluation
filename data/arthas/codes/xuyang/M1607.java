    public static List<AdviceListener> queryAdviceListeners(ClassLoader classLoader, String className,
            String methodName, String methodDesc) {
        classLoader = wrap(classLoader);
        className = className.replace('/', '.');
        ClassLoaderAdviceListenerManager manager = adviceListenerMap.get(classLoader);

        if (manager != null) {
            return manager.queryAdviceListeners(className, methodName, methodDesc);
        }

        return null;
    }
