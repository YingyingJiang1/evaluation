  @Override
  public List<StreamEntry> xrevrange(final String key, final String end, final String start, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xrevrange(key, end, start, count));
  }
