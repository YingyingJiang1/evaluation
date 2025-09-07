  @Override
  public long smove(final String srckey, final String dstkey, final String member) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.smove(srckey, dstkey, member));
  }
