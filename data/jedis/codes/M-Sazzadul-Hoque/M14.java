  @Override
  public int flush() {
    lock.lock();
    try {
      int result = this.getSize();
      clearStore();
      redisKeysToCacheKeys.clear();
      getEvictionPolicy().resetAll();
      getStats().flush();
      return result;
    } finally {
      lock.unlock();
    }
  }
