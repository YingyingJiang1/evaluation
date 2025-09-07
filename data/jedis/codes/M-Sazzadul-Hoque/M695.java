  @Override
  public List<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeByScoreWithScores(key, max, min));
  }
