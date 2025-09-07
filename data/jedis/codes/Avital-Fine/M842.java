  @Override
  public String functionDelete(final byte[] libraryName) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionDelete(libraryName));
  }
