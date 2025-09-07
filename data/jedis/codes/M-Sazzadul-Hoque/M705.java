  @Override
  public long zunionstore(final String dstkey, final String... sets) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zunionstore(dstkey, sets));
  }
