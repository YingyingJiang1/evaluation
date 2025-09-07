  @Override
  @Deprecated
  public long zdiffStore(final String dstkey, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zdiffStore(dstkey, keys));
  }
