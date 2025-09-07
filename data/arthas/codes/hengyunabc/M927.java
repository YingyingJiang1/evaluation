    public static Map<String, Map<String, Object>> getLoggers(String name, boolean includeNoAppender) {
        Map<String, Map<String, Object>> loggerInfoMap = new HashMap<String, Map<String, Object>>();
        if (!Log4j) {
            return loggerInfoMap;
        }

        if (name != null && !name.trim().isEmpty()) {
            Logger logger = LogManager.getLoggerRepository().exists(name);
            if (logger != null) {
                loggerInfoMap.put(name, doGetLoggerInfo(logger));
            }
        } else {
            // 获取所有logger时，如果没有appender则忽略
            @SuppressWarnings("unchecked")
            Enumeration<Logger> loggers = LogManager.getLoggerRepository().getCurrentLoggers();

            if (loggers != null) {
                while (loggers.hasMoreElements()) {
                    Logger logger = loggers.nextElement();
                    Map<String, Object> info = doGetLoggerInfo(logger);
                    if (!includeNoAppender) {
                        List<?> appenders = (List<?>) info.get(LoggerHelper.appenders);
                        if (appenders != null && !appenders.isEmpty()) {
                            loggerInfoMap.put(logger.getName(), info);
                        }
                    } else {
                        loggerInfoMap.put(logger.getName(), info);
                    }
                }
            }

            Logger root = LogManager.getLoggerRepository().getRootLogger();
            if (root != null) {
                Map<String, Object> info = doGetLoggerInfo(root);
                if (!includeNoAppender) {
                    List<?> appenders = (List<?>) info.get(LoggerHelper.appenders);
                    if (appenders != null && !appenders.isEmpty()) {
                        loggerInfoMap.put(root.getName(), info);
                    }
                } else {
                    loggerInfoMap.put(root.getName(), info);
                }
            }
        }

        return loggerInfoMap;
    }
