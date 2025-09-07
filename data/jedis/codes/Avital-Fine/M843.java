  @Override
  public byte[] functionDump() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionDump());
  }
