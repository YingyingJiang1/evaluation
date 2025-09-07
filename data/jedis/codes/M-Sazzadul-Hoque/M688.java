  @Override
  public List<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeByScoreWithScores(key, min, max));
  }
