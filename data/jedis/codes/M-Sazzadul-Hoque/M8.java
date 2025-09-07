  @Override
  public CacheEntry get(CacheKey cacheKey) {
    CacheEntry entry = getFromStore(cacheKey);
    if (entry != null) {
      getEvictionPolicy().touch(cacheKey);
    }
    return entry;
  }
