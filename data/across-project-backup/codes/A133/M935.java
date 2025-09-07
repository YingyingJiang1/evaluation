    private static List<Map<String, Object>> doGetLoggerAppenders(LoggerConfig loggerConfig) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, Appender> appenders = loggerConfig.getAppenders();

        for (Entry<String, Appender> entry : appenders.entrySet()) {
            Map<String, Object> info = new HashMap<String, Object>();
            Appender appender = entry.getValue();
            info.put(LoggerHelper.name, appender.getName());
            info.put(LoggerHelper.clazz, appender.getClass());

            result.add(info);
            if (appender instanceof FileAppender) {
                info.put(LoggerHelper.file, ((FileAppender) appender).getFileName());
            } else if (appender instanceof ConsoleAppender) {
                info.put(LoggerHelper.target, ((ConsoleAppender) appender).getTarget());
            } else if (appender instanceof AsyncAppender) {

                AsyncAppender asyncAppender = ((AsyncAppender) appender);
                String[] appenderRefStrings = asyncAppender.getAppenderRefStrings();

                info.put(LoggerHelper.blocking, asyncAppender.isBlocking());
                info.put(LoggerHelper.appenderRef, Arrays.asList(appenderRefStrings));
            }
        }
        return result;
    }
