    private static EvictionPolicy getEvictionPolicy(CacheConfig config) {
        if (config.getEvictionPolicy() == null) {
            // It will be default to LRUEviction, until we have other eviction implementations
            return new LRUEviction(config.getMaxSize());
        }
        return config.getEvictionPolicy();
    }
