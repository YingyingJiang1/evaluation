  @Override
  public List<Map.Entry<byte[], List<StreamEntryBinary>>> xreadBinary(XReadParams xReadParams,
      Map<byte[], StreamEntryID> streams) {
    return executeCommand(commandObjects.xreadBinary(xReadParams, streams));
  }
