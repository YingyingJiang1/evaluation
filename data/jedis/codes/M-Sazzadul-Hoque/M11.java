  @Override
  public List<Boolean> delete(List<CacheKey> cacheKeys) {
    lock.lock();
    try {
      return cacheKeys.stream().map(this::delete).collect(Collectors.toList());
    } finally {
      lock.unlock();
    }
  }
