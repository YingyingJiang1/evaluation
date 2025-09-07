  @Override
  public Response<Map<byte[], List<StreamEntryBinary>>> xreadBinaryAsMap(XReadParams xReadParams,
      Map<byte[], StreamEntryID> streams) {
    return appendCommand(commandObjects.xreadBinaryAsMap(xReadParams, streams));
  }
