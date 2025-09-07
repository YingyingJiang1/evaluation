  @Override
  public String functionLoadReplace(final byte[] functionCode) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionLoadReplace(functionCode));
  }
