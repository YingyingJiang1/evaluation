  @Override
  public String functionRestore(final byte[] serializedValue, final FunctionRestorePolicy policy) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.functionRestore(serializedValue, policy));
  }
