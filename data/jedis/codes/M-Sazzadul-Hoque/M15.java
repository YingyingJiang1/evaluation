  @Override
  public CacheStats getAndResetStats() {
    CacheStats result = stats;
    stats = new CacheStats();
    return result;
  }
