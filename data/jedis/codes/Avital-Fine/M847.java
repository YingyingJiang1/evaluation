  @Override
  public List<Object> functionListWithCode(final byte[] libraryNamePattern) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionListWithCode(libraryNamePattern));
  }
