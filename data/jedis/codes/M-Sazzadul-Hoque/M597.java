  @Override
  public long llen(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.llen(key));
  }
