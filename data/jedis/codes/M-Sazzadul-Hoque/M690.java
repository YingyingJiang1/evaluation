  @Override
  public List<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max,
      final int offset, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
  }
