  @Override
  public long hset(final String key, final String field, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hset(key, field, value));
  }
