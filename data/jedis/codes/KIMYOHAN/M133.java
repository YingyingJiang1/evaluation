  @Override
  public Response<List<Map.Entry<byte[], List<StreamEntryBinary>>>> xreadBinary(XReadParams xReadParams,
      Map<byte[], StreamEntryID> streams) {
    return appendCommand(commandObjects.xreadBinary(xReadParams, streams));
  }
