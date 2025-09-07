  @Override
  public long expire(final String key, final long seconds, final ExpiryOption expiryOption) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.expire(key, seconds, expiryOption));
  }
