  @Override
  public String type(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.type(key));
  }
