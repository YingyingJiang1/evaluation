  @Override
  public long setnx(final String key, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.setnx(key, value));
  }
