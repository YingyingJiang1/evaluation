  public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
    try (Connection connection = this.provider.getConnection()) {
      jedisPubSub.proceed(connection, channels);
    }
  }
