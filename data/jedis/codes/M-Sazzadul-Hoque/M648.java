  @Override
  public List<Tuple> zrangeWithScores(final String key, final long start, final long stop) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeWithScores(key, start, stop));
  }
