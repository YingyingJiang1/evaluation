  @Override
  public Object xinfoStream(byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xinfoStream(key));
  }
