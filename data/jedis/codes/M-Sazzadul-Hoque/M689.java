  @Override
  public List<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeByScoreWithScores(key, min, max));
  }
