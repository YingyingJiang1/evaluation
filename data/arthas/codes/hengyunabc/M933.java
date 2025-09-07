    private static Object getConfigField(LoggerConfig loggerConfig) {
        try {
            if (configField != null) {
                return configField.get(loggerConfig);
            }
        } catch (Throwable e) {
            // ignore
        }
        return null;
    }
