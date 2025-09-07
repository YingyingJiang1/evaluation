    @Override
    public Collection<String> allAgentIds() {
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
        return (Collection<String>) (Collection<?>) nativeCache.asMap().keySet();
    }
