  @Override
  public List<Tuple> zrangeWithScores(String key, ZRangeParams zRangeParams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeWithScores(key, zRangeParams));
  }
