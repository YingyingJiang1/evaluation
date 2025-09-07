  @Override
  public String functionLoadReplace(final String functionCode) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionLoadReplace(functionCode));
  }
