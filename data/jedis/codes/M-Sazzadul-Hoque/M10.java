  @Override
  public boolean delete(CacheKey cacheKey) {
    lock.lock();
    try {
      boolean removed = removeFromStore(cacheKey);
      getEvictionPolicy().reset(cacheKey);

      // removing it from redisKeysToCacheKeys as well
      // TODO: considering not doing it, what is the impact of not doing it ??
      for (Object redisKey : cacheKey.getRedisKeys()) {
        ByteBuffer mapKey = makeKeyForRedisKeysToCacheKeys(redisKey);
        Set<CacheKey<?>> cacheKeysRelatedtoRedisKey = redisKeysToCacheKeys.get(mapKey);
        if (cacheKeysRelatedtoRedisKey != null) {
          cacheKeysRelatedtoRedisKey.remove(cacheKey);
        }
      }
      return removed;
    } finally {
      lock.unlock();
    }
  }
