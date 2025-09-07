  @Override
  public String spop(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.spop(key));
  }
