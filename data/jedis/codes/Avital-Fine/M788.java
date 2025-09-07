  @Override
  public List<LibraryInfo> functionList(final String libraryNamePattern) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionList(libraryNamePattern));
  }
