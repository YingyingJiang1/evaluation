  @Override
  public List<Object> functionList(final byte[] libraryNamePattern) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionList(libraryNamePattern));
  }
