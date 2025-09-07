  @Override
  public List<CacheKey> deleteByRedisKey(Object key) {
    lock.lock();
    try {
      final ByteBuffer mapKey = makeKeyForRedisKeysToCacheKeys(key);

      Set<CacheKey<?>> commands = redisKeysToCacheKeys.get(mapKey);
      List<CacheKey> cacheKeys = new ArrayList<>();
      if (commands != null) {
        cacheKeys.addAll(commands.stream().filter(this::removeFromStore).collect(Collectors.toList()));
        stats.invalidationByServer(cacheKeys.size());
        redisKeysToCacheKeys.remove(mapKey);
      }
      stats.invalidationMessages();
      return cacheKeys;
    } finally {
      lock.unlock();
    }
  }
