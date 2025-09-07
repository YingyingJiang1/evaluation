  @Override
  public long sdiffstore(final String dstkey, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sdiffstore(dstkey, keys));
  }
