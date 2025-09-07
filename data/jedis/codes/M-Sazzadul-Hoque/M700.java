  @Override
  public long zremrangeByRank(final String key, final long start, final long stop) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zremrangeByRank(key, start, stop));
  }
