    private static List<Map<String, Object>> doGetLoggerAppenders(Enumeration<Appender> appenders) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        if (appenders == null) {
            return result;
        }

        while (appenders.hasMoreElements()) {
            Map<String, Object> info = new HashMap<String, Object>();
            Appender appender = appenders.nextElement();

            info.put(LoggerHelper.name, appender.getName());
            info.put(LoggerHelper.clazz, appender.getClass());

            result.add(info);
            if (appender instanceof FileAppender) {
                info.put(LoggerHelper.file, ((FileAppender) appender).getFile());
            } else if (appender instanceof ConsoleAppender) {
                info.put(LoggerHelper.target, ((ConsoleAppender) appender).getTarget());
            } else if (appender instanceof AsyncAppender) {
                @SuppressWarnings("unchecked")
                Enumeration<Appender> appendersOfAsync = ((AsyncAppender) appender).getAllAppenders();
                if (appendersOfAsync != null) {
                    List<Map<String, Object>> asyncs = doGetLoggerAppenders(appendersOfAsync);
                    // 标明异步appender
                    List<String> appenderRef = new ArrayList<String>();
                    for (Map<String, Object> a : asyncs) {
                        appenderRef.add((String) a.get(LoggerHelper.name));
                        result.add(a);
                    }
                    info.put(LoggerHelper.blocking, ((AsyncAppender) appender).getBlocking());
                    info.put(LoggerHelper.appenderRef, appenderRef);
                }
            }
        }

        return result;
    }
