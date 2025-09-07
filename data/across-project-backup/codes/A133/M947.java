    public void loggers(CommandProcess process) {
        Map<ClassLoader, LoggerTypes> classLoaderLoggerMap = new LinkedHashMap<ClassLoader, LoggerTypes>();

        // 如果不指定 classloader，则打印所有 classloader 里的 logger 信息
        for (Class<?> clazz : process.session().getInstrumentation().getAllLoadedClasses()) {
            String className = clazz.getName();
            ClassLoader classLoader = clazz.getClassLoader();

            // if special classloader
            if (this.hashCode != null && !this.hashCode.equals(StringUtils.classLoaderHash(clazz))) {
                continue;
            }

            if (classLoader != null) {
                LoggerTypes loggerTypes = classLoaderLoggerMap.get(classLoader);
                if (loggerTypes == null) {
                    loggerTypes = new LoggerTypes();
                    classLoaderLoggerMap.put(classLoader, loggerTypes);
                }
                updateLoggerType(loggerTypes, classLoader, className);
            }
        }

        for (Entry<ClassLoader, LoggerTypes> entry : classLoaderLoggerMap.entrySet()) {
            ClassLoader classLoader = entry.getKey();
            LoggerTypes loggerTypes = entry.getValue();

            if (loggerTypes.contains(LoggerType.LOG4J)) {
                Map<String, Map<String, Object>> loggerInfoMap = loggerInfo(classLoader, Log4jHelper.class);
                process.appendResult(new LoggerModel(loggerInfoMap));
            }

            if (loggerTypes.contains(LoggerType.LOGBACK)) {
                Map<String, Map<String, Object>> loggerInfoMap = loggerInfo(classLoader, LogbackHelper.class);
                process.appendResult(new LoggerModel(loggerInfoMap));
            }

            if (loggerTypes.contains(LoggerType.LOG4J2)) {
                Map<String, Map<String, Object>> loggerInfoMap = loggerInfo(classLoader, Log4j2Helper.class);
                process.appendResult(new LoggerModel(loggerInfoMap));
            }
        }

        process.end();
    }
