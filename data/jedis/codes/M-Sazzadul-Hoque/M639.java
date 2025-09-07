  @Override
  public List<String> zrange(final String key, final long start, final long stop) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrange(key, start, stop));
  }
