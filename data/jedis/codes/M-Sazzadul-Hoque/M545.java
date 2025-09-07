  @Override
  public String randomKey() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.randomKey());
  }
