    @Override
    public synchronized CacheKey evictNext() {
        CacheKey cacheKey = pendingEvictions.pollFirst();
        while (cacheKey != null && !cache.hasCacheKey(cacheKey)) {
            cacheKey = pendingEvictions.pollFirst();
        }
        return cacheKey;
    }
