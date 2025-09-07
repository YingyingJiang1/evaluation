  @Override
  public long zrem(final String key, final String... members) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrem(key, members));
  }
