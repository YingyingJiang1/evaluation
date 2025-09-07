  @Override
  public long zadd(final String key, final double score, final String member,
      final ZAddParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zadd(key, score, member, params));
  }
