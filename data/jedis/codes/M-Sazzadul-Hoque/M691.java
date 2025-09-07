  @Override
  public List<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max,
      final int offset, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
  }
