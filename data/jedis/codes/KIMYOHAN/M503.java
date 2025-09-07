  @Override
  public List<Map.Entry<byte[], List<StreamEntryBinary>>> xreadBinary(XReadParams xReadParams,
      Map<byte[], StreamEntryID> streams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xreadBinary(xReadParams, streams));
  }
