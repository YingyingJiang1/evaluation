  @Override
  public long zinterstore(final byte[] dstkey, final byte[]... sets) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zinterstore(dstkey, sets));
  }
