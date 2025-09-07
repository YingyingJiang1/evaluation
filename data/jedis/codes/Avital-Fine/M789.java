  @Override
  public List<LibraryInfo> functionListWithCode(String libraryNamePattern) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionListWithCode(libraryNamePattern));
  }
