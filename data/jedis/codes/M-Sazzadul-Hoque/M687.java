  @Override
  public List<String> zrangeByScore(final String key, final String min, final String max,
      final int offset, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeByScore(key, min, max, offset, count));
  }
