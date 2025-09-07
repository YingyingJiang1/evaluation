  @Override
  public long bitop(final BitOP op, final byte[] destKey, final byte[]... srcKeys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bitop(op, destKey, srcKeys));
  }
