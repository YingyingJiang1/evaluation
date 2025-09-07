  public void psubscribe(BinaryJedisPubSub jedisPubSub, final byte[]... patterns) {
    try (Connection connection = this.provider.getConnection()) {
      jedisPubSub.proceedWithPatterns(connection, patterns);
    }
  }
