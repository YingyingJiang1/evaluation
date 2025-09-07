    public static FieldCache declaredFields(Class<?> clazz, ConfigurationHolder configurationHolder) {
        switch (configurationHolder.globalConfiguration().getFiledCacheLocation()) {
            case THREAD_LOCAL:
                Map<FieldCacheKey, FieldCache> fieldCacheMap = FIELD_THREAD_LOCAL.get();
                if (fieldCacheMap == null) {
                    fieldCacheMap = MapUtils.newHashMap();
                    FIELD_THREAD_LOCAL.set(fieldCacheMap);
                }
                return fieldCacheMap.computeIfAbsent(new FieldCacheKey(clazz, configurationHolder), key -> {
                    return doDeclaredFields(clazz, configurationHolder);
                });
            case MEMORY:
                return FIELD_CACHE.computeIfAbsent(new FieldCacheKey(clazz, configurationHolder), key -> {
                    return doDeclaredFields(clazz, configurationHolder);
                });
            case NONE:
                return doDeclaredFields(clazz, configurationHolder);
            default:
                throw new UnsupportedOperationException("unsupported enum");
        }
    }
