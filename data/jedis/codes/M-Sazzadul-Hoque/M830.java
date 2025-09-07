  @Override
  public List<StreamEntry> xrevrange(final String key, final String end, final String start) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xrevrange(key, end, start));
  }
