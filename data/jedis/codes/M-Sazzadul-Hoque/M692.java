  @Override
  public List<String> zrevrangeByScore(final String key, final double max, final double min) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeByScore(key, max, min));
  }
