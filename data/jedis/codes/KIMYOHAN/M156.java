  @Override
  public Map<byte[], List<StreamEntryBinary>> xreadGroupBinaryAsMap(byte[] groupName,
      byte[] consumer, XReadGroupParams xReadGroupParams, Map<byte[], StreamEntryID> streams) {
    return executeCommand(
        commandObjects.xreadGroupBinaryAsMap(groupName, consumer, xReadGroupParams, streams));
  }
