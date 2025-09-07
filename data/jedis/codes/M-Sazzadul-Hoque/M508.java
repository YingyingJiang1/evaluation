  @Override
  public long xlen(byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xlen(key));
  }
