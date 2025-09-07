    @SuppressWarnings("rawtypes")
    private static List<Map<String, Object>> doGetLoggerAppenders(Iterator<Appender<ILoggingEvent>> appenders) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        while (appenders.hasNext()) {
            Map<String, Object> info = new LinkedHashMap<String, Object>();
            Appender<ILoggingEvent> appender = appenders.next();
            info.put(LoggerHelper.name, appender.getName());
            info.put(LoggerHelper.clazz, appender.getClass());
            if (appender instanceof FileAppender) {
                info.put(LoggerHelper.file, ((FileAppender) appender).getFile());
            } else if (appender instanceof AsyncAppender) {
                AsyncAppender aa = (AsyncAppender) appender;
                Iterator<Appender<ILoggingEvent>> iter = aa.iteratorForAppenders();
                List<Map<String, Object>> asyncs = doGetLoggerAppenders(iter);

                // 异步appender所 ref的 appender，参考： https://logback.qos.ch/manual/appenders.html
                List<String> appenderRef = new ArrayList<String>();
                for (Map<String, Object> a : asyncs) {
                    appenderRef.add((String) a.get(LoggerHelper.name));
                    result.add(a);
                }
                info.put(LoggerHelper.appenderRef, appenderRef);
                info.put(LoggerHelper.blocking, !aa.isNeverBlock());
            } else if (appender instanceof ConsoleAppender) {
                info.put(LoggerHelper.target, ((ConsoleAppender) appender).getTarget());
            }
            result.add(info);
        }

        return result;
    }
