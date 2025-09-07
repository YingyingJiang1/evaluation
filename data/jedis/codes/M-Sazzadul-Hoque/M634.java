  @Override
  public Double zaddIncr(final String key, final double score, final String member, final ZAddParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zaddIncr(key, score, member, params));
  }
