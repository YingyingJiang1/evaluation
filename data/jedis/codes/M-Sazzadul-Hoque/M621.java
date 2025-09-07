  @Override
  public long sinterstore(final String dstkey, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sinterstore(dstkey, keys));
  }
