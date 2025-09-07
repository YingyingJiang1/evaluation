  @Override
  public long rpush(final String key, final String... strings) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.rpush(key, strings));
  }
