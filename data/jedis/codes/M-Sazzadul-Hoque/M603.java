  @Override
  public String lpop(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lpop(key));
  }
