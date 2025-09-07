  @Override
  public String functionDelete(final String libraryName) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionDelete(libraryName));
  }
