  @Override
  public <T> T executeCommand(final CommandObject<T> commandObject) {
    final CacheKey cacheKey = new CacheKey(commandObject);
    if (!cache.isCacheable(cacheKey)) {
      cache.getStats().nonCacheable();
      return super.executeCommand(commandObject);
    }

    CacheEntry<T> cacheEntry = cache.get(cacheKey);
    if (cacheEntry != null) { // (probable) CACHE HIT !!
      cacheEntry = validateEntry(cacheEntry);
      if (cacheEntry != null) {
        // CACHE HIT confirmed !!!
        cache.getStats().hit();
        return cacheEntry.getValue();
      }
    }

    // CACHE MISS !!
    cache.getStats().miss();
    T value = super.executeCommand(commandObject);
    cacheEntry = new CacheEntry<>(cacheKey, value, this);
    cache.set(cacheKey, cacheEntry);
    // this line actually provides a deep copy of cached object instance 
    value = cacheEntry.getValue();
    return value;
  }
