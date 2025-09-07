  @Override
  public void close() {
    sentinelListeners.forEach(SentinelListener::shutdown);

    pool.close();
  }
