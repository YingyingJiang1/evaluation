    private static Cache instantiateCustomCache(CacheConfig config) {
        try {
            if (config.getCacheable() != null) {
                Constructor ctorWithCacheable = findConstructorWithCacheable(config.getCacheClass());
                if (ctorWithCacheable != null) {
                    return (Cache) ctorWithCacheable.newInstance(config.getMaxSize(), getEvictionPolicy(config), config.getCacheable());
                }
            }
            Constructor ctor = getConstructor(config.getCacheClass());
            return (Cache) ctor.newInstance(config.getMaxSize(), getEvictionPolicy(config));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | SecurityException e) {
            throw new JedisCacheException("Failed to insantiate custom cache type!", e);
        }
    }
