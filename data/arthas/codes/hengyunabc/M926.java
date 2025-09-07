    public static Boolean updateLevel(String name, String level) {
        if (Log4j) {
            Level l = Level.toLevel(level, Level.ERROR);
            Logger logger = LogManager.getLoggerRepository().exists(name);
            if (logger != null) {
                logger.setLevel(l);
                return true;
            } else {
                Logger root = LogManager.getLoggerRepository().getRootLogger();
                if (root.getName().equals(name)) {
                    root.setLevel(l);
                    return true;
                }
            }
            return false;
        }
        return null;
    }
