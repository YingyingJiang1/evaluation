  @Override
  public long expireAt(String key, long unixTime, ExpiryOption expiryOption) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.expireAt(key, unixTime, expiryOption));
  }
