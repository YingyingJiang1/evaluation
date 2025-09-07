  @Override
  public byte[] blmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to, double timeout) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blmove(srcKey, dstKey, from, to, timeout));
  }
