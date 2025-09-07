  @Override
  public String functionLoad(final String functionCode) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionLoad(functionCode));
  }
