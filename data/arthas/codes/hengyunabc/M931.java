    public static Boolean updateLevel(String loggerName, String logLevel) {
        if (Log4j2) {
            Level level = Level.getLevel(logLevel.toUpperCase());
            if (level == null) {
                return null;
            }
            LoggerConfig loggerConfig = getLoggerConfig(loggerName);
            if (loggerConfig == null) {
                loggerConfig = new LoggerConfig(loggerName, level, true);
                getLoggerContext().getConfiguration().addLogger(loggerName, loggerConfig);
            } else {
                loggerConfig.setLevel(level);
            }
            getLoggerContext().updateLoggers();
            return Boolean.TRUE;
        }
        return null;
    }
