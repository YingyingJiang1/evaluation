    public static Cache getCache(CacheConfig config) {
        if (config.getCacheClass() == null) {
            if (config.getCacheable() == null) {
                throw new JedisCacheException("Cacheable is required to create the default cache!");
            }
            return new DefaultCache(config.getMaxSize(), config.getCacheable(), getEvictionPolicy(config));
        }
        return instantiateCustomCache(config);
    }
