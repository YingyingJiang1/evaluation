  @Override
  public List<String> zrevrange(final String key, final long start, final long stop) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrange(key, start, stop));
  }
