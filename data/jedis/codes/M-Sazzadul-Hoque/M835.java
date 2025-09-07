  @Override
  public Map<String, List<StreamEntry>> xreadGroupAsMap(final String groupName, final String consumer,
      final XReadGroupParams xReadGroupParams, final Map<String, StreamEntryID> streams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xreadGroupAsMap(groupName, consumer, xReadGroupParams, streams));
  }
