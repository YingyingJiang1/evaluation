  @Override
  public String functionLoad(final byte[] functionCode) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionLoad(functionCode));
  }
