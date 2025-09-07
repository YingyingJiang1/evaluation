  @Override
  public Map<byte[], List<StreamEntryBinary>> xreadBinaryAsMap(XReadParams xReadParams,
      Map<byte[], StreamEntryID> streams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xreadBinaryAsMap(xReadParams, streams));
  }
