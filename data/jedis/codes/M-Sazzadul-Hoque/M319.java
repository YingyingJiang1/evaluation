  @Override
  public long zunionstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zunionstore(dstkey, params, sets));
  }
