  @Override
  public long hincrBy(final String key, final String field, final long value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hincrBy(key, field, value));
  }
