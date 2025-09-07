  @Override
  public long lrem(final String key, final long count, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lrem(key, count, value));
  }
