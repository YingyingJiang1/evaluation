  @Override
  public Map<byte[], List<StreamEntryBinary>> xreadBinaryAsMap(XReadParams xReadParams,
      Map<byte[], StreamEntryID> streams) {
    return executeCommand(commandObjects.xreadBinaryAsMap(xReadParams, streams));
  }
