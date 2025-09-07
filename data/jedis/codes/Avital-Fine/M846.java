  @Override
  public List<Object> functionListWithCodeBinary() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionListWithCodeBinary());
  }
