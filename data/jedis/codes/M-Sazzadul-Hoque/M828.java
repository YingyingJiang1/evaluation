  @Override
  public List<StreamEntry> xrange(final String key, final String start, final String end) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xrange(key, start, end));
  }
