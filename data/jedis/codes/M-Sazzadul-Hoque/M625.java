  @Override
  public long sunionstore(final String dstkey, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sunionstore(dstkey, keys));
  }
