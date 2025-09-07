  @Override
  public List<String> zrevrangeByScore(final String key, final String max, final String min,
      final int offset, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeByScore(key, max, min, offset, count));
  }
