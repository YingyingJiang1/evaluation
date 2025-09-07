  @Override
  public byte[] lmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lmove(srcKey, dstKey, from, to));
  }
