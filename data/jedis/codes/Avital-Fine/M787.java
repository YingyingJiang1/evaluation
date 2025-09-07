  @Override
  public List<LibraryInfo> functionList() {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionList());
  }
