  @Override
  protected Object protocolRead(RedisInputStream inputStream) {
    lock.lock();
    try {
      return Protocol.read(inputStream, cache);
    } finally {
      lock.unlock();
    }
  }
