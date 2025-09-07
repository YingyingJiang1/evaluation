  @Override
  public long zinterstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zinterstore(dstkey, params, sets));
  }
