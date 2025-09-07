  public void subscribe(BinaryJedisPubSub jedisPubSub, final byte[]... channels) {
    try (Connection connection = this.provider.getConnection()) {
      jedisPubSub.proceed(connection, channels);
    }
  }
