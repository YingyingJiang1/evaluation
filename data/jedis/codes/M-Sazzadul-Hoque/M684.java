  @Override
  public List<String> zrangeByScore(final String key, final double min, final double max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeByScore(key, min, max));
  }
