  @Override
  public long touch(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.touch(key));
  }
