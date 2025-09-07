  @Override
  public List<StreamEntry> xrange(final String key, final String start, final String end, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xrange(key, start, end, count));
  }
