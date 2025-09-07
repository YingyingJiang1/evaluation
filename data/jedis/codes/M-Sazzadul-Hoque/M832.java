  @Override
  public List<Map.Entry<String, List<StreamEntry>>> xread(final XReadParams xReadParams, final Map<String, StreamEntryID> streams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xread(xReadParams, streams));
  }
