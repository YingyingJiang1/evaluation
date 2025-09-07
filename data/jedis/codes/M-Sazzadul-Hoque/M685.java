  @Override
  public List<String> zrangeByScore(final String key, final String min, final String max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeByScore(key, min, max));
  }
