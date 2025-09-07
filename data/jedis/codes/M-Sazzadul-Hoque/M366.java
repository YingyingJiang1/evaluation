  @Override
  public long bitpos(final byte[] key, final boolean value, final BitPosParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bitpos(key, value, params));
  }
