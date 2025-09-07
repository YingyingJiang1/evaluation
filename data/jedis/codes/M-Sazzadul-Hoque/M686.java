  @Override
  public List<String> zrangeByScore(final String key, final double min, final double max,
      final int offset, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeByScore(key, min, max, offset, count));
  }
