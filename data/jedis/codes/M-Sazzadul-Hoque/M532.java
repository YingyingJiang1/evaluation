  @Override
  public String get(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.get(key));
  }
