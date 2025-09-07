    @Override
    public void setCache(Cache cache) {
        this.cache = cache;
        this.accessTimes = new LinkedHashMap<CacheKey, Long>(initialCapacity, 1f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<CacheKey, Long> eldest) {
                boolean evictionRequired = cache.getSize() > cache.getMaxSize()
                        || accessTimes.size() > cache.getMaxSize();
                // here the cache check is only for performance gain; we are trying to avoid the sequence add + poll + hasCacheKey
                // and prefer to check it in cache once in early stage.
                // if there is nothing to remove in actual cache as of now, stop worrying about it.
                if (evictionRequired && cache.hasCacheKey(eldest.getKey())) {
                    pendingEvictions.addLast(eldest.getKey());

                }
                return evictionRequired;
            }
        };
    }
