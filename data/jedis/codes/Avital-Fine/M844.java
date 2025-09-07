  @Override
  public List<Object> functionListBinary() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionListBinary());
  }
