    public static Boolean updateLevel(String name, String level) {
        if (Logback) {
            try {
                Level l = Level.toLevel(level, Level.ERROR);
                LoggerContext loggerContext = (LoggerContext) loggerFactoryInstance;

                Logger logger = loggerContext.exists(name);
                if (logger != null) {
                    logger.setLevel(l);
                    return true;
                }
            } catch (Throwable t) {
                // ignore
            }
            return false;
        }
        return null;
    }
