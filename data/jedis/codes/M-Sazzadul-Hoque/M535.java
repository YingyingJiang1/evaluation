  @Override
  public String getDel(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.getDel(key));
  }
