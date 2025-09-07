  @Override
  public long expireTime(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.expireTime(key));
  }
