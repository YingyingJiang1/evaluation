  @Override
  public List<String> zrevrangeByScore(final String key, final double max, final double min,
      final int offset, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeByScore(key, max, min, offset, count));
  }
