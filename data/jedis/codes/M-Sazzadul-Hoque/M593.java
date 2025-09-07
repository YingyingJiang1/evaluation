  @Override
  public String hrandfield(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hrandfield(key));
  }
