  @Override
  public Response<Map<byte[], List<StreamEntryBinary>>> xreadGroupBinaryAsMap(byte[] groupName,
      byte[] consumer, XReadGroupParams xReadGroupParams, Map<byte[], StreamEntryID> streams) {
    return appendCommand(
        commandObjects.xreadGroupBinaryAsMap(groupName, consumer, xReadGroupParams, streams));
  }
