  @Override
  public long sadd(final String key, final String... members) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sadd(key, members));
  }
