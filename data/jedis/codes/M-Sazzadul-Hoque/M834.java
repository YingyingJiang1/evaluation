  @Override
  public List<Map.Entry<String, List<StreamEntry>>> xreadGroup(final String groupName, final String consumer,
      final XReadGroupParams xReadGroupParams, final Map<String, StreamEntryID> streams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, streams));
  }
