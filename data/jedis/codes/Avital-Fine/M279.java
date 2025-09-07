  @Override
  public long expire(final byte[] key, final long seconds, final ExpiryOption expiryOption) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand((commandObjects.expire(key, seconds, expiryOption)));
  }
