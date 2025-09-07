    private LoggerTypes findLoggerTypes(Instrumentation inst, ClassLoader classLoader) {
        LoggerTypes loggerTypes = new LoggerTypes();
        for (Class<?> clazz : inst.getAllLoadedClasses()) {
            if(classLoader == clazz.getClassLoader()) {
                updateLoggerType(loggerTypes, classLoader, clazz.getName());
            }
        }
        return loggerTypes;
    }
