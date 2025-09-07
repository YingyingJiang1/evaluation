  @Override
  public Object functionStatsBinary() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionStatsBinary());
  }
