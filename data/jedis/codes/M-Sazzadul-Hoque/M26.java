  @Override
  protected void protocolReadPushes(RedisInputStream inputStream) {
    if (lock.tryLock()) {
      try {
        Protocol.readPushes(inputStream, cache, true);
      } finally {
        lock.unlock();
      }
    }
  }
