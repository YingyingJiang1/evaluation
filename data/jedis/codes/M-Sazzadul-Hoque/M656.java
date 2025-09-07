  @Override
  public long zcard(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zcard(key));
  }
