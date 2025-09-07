  @Override
  public long lpush(final String key, final String... strings) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lpush(key, strings));
  }
