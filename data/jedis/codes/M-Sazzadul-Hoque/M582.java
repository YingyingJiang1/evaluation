  @Override
  public long hsetnx(final String key, final String field, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hsetnx(key, field, value));
  }
