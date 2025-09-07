  @Override
  public String memoryPurge() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(MEMORY, PURGE);
    return connection.getBulkReply();
  }
