  @Override
  public boolean exists(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.exists(key));
  }
