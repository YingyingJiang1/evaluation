  @Override
  public long hlen(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hlen(key));
  }
