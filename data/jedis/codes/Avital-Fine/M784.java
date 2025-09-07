  @Override
  public String functionFlush() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionFlush());
  }
