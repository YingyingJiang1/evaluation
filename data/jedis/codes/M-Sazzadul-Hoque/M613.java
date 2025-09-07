  @Override
  public long srem(final String key, final String... members) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.srem(key, members));
  }
