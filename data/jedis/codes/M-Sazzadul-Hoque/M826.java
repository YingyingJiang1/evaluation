  @Override
  public List<StreamEntry> xrange(final String key, final StreamEntryID start, final StreamEntryID end) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xrange(key, start, end));
  }
