  @Override
  public List<Tuple> zrevrangeWithScores(final String key, final long start, final long stop) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeWithScores(key, start, stop));
  }
