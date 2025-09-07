  @Override
  public long zdiffstore(final byte[] dstkey, final byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zdiffstore(dstkey, keys));
  }
