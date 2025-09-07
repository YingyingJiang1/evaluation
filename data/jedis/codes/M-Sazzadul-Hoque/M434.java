  @Override
  public String clientUnpause() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, UNPAUSE);
    return connection.getBulkReply();
  }
