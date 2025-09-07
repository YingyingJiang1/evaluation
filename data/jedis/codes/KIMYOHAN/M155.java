  @Override
  public List<Map.Entry<byte[], List<StreamEntryBinary>>> xreadGroupBinary(byte[] groupName,
      byte[] consumer, XReadGroupParams xReadGroupParams, Map<byte[], StreamEntryID> streams) {
    return executeCommand(
        commandObjects.xreadGroupBinary(groupName, consumer, xReadGroupParams, streams));
  }
