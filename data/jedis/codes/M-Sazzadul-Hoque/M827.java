  @Override
  public List<StreamEntry> xrevrange(final String key, final StreamEntryID end,
      final StreamEntryID start) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xrevrange(key, end, start));
  }
