  @Override
  public List<String> zrevrangeByScore(final String key, final String max, final String min) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeByScore(key, max, min));
  }
