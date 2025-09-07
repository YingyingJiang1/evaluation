  @Override
  public long expireAt(final String key, final long unixTime) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.expireAt(key, unixTime));
  }
