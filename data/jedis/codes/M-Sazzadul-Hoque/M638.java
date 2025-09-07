  @Override
  public long zdiffstore(final String dstkey, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zdiffstore(dstkey, keys));
  }
