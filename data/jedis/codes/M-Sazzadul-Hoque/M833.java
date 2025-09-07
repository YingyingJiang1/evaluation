  @Override
  public Map<String, List<StreamEntry>> xreadAsMap(final XReadParams xReadParams, final Map<String, StreamEntryID> streams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xreadAsMap(xReadParams, streams));
  }
