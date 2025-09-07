  @Override
  public List<Tuple> zrandmemberWithScores(final String key, final long count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrandmemberWithScores(key, count));
  }
