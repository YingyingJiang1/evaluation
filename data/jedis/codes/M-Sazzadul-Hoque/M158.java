  public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
    try (Connection connection = this.provider.getConnection()) {
      jedisPubSub.proceedWithPatterns(connection, patterns);
    }
  }
