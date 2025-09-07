  @Override
  public List<Tuple> zrangeWithScores(byte[] key, ZRangeParams zRangeParams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeWithScores(key, zRangeParams));
  }
