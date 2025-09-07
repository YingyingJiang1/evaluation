  @Override
  public long zremrangeByScore(final String key, final String min, final String max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zremrangeByScore(key, min, max));
  }
