  @Override
  public List<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeByScoreWithScores(key, max, min));
  }
