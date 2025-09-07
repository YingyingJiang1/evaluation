  @Override
  public List<CacheKey> deleteByRedisKeys(List keys) {
    if (keys == null) {
      flush();
      return null;
    }
    lock.lock();
    try {
      return ((List<Object>) keys).stream()
          .map(this::deleteByRedisKey).flatMap(List::stream).collect(Collectors.toList());
    } finally {
      lock.unlock();
    }
  }
