  @Override
  public long zremrangeByScore(final String key, final double min, final double max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zremrangeByScore(key, min, max));
  }
