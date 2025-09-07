    private static Map<String, Object> doGetLoggerInfo(LoggerConfig loggerConfig) {
        Map<String, Object> info = new HashMap<String, Object>();

        String name = loggerConfig.getName();
        if (name == null || name.trim().isEmpty()) {
            name = LoggerConfig.ROOT;
        }

        info.put(LoggerHelper.name, name);
        info.put(LoggerHelper.clazz, loggerConfig.getClass());
        CodeSource codeSource = loggerConfig.getClass().getProtectionDomain().getCodeSource();
        if (codeSource != null) {
            info.put(LoggerHelper.codeSource, codeSource.getLocation());
        }
        Object config = getConfigField(loggerConfig);
        if (config != null) {
            info.put(LoggerHelper.config, config);
        }

        info.put(LoggerHelper.additivity, loggerConfig.isAdditive());

        Level level = loggerConfig.getLevel();
        if (level != null) {
            info.put(LoggerHelper.level, level.toString());
        }

        List<Map<String, Object>> result = doGetLoggerAppenders(loggerConfig);
        info.put(LoggerHelper.appenders, result);
        return info;
    }
