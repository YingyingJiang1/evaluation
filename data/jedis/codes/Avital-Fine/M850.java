  @Override
  public String functionRestore(final byte[] serializedValue) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionRestore(serializedValue));
  }
