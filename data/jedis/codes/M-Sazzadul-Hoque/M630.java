  @Override
  public long zadd(final String key, final double score, final String member) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zadd(key, score, member));
  }
