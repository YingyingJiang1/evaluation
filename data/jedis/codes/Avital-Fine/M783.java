  @Override
  public FunctionStats functionStats() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionStats());
  }
