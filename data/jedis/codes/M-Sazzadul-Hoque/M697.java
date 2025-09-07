  @Override
  public List<Tuple> zrevrangeByScoreWithScores(final String key, final String max,
      final String min, final int offset, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
  }
