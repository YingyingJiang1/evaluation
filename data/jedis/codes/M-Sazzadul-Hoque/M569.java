  @Override
  public long decr(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.decr(key));
  }
