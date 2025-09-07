  @Override
  public List<String> lrange(final String key, final long start, final long stop) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lrange(key, start, stop));
  }
