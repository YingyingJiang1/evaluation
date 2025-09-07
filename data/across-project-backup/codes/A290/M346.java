    private static ExcelContentProperty getExcelContentProperty(Class<?> clazz, Class<?> headClass, String fieldName,
        ConfigurationHolder configurationHolder) {
        switch (configurationHolder.globalConfiguration().getFiledCacheLocation()) {
            case THREAD_LOCAL:
                Map<ContentPropertyKey, ExcelContentProperty> contentCacheMap = CONTENT_THREAD_LOCAL.get();
                if (contentCacheMap == null) {
                    contentCacheMap = MapUtils.newHashMap();
                    CONTENT_THREAD_LOCAL.set(contentCacheMap);
                }
                return contentCacheMap.computeIfAbsent(buildKey(clazz, headClass, fieldName), key -> {
                    return doGetExcelContentProperty(clazz, headClass, fieldName, configurationHolder);
                });
            case MEMORY:
                return CONTENT_CACHE.computeIfAbsent(buildKey(clazz, headClass, fieldName), key -> {
                    return doGetExcelContentProperty(clazz, headClass, fieldName, configurationHolder);
                });
            case NONE:
                return doGetExcelContentProperty(clazz, headClass, fieldName, configurationHolder);
            default:
                throw new UnsupportedOperationException("unsupported enum");
        }
    }
