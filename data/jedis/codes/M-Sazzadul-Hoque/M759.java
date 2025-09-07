  @Override
  public String clientList() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, LIST);
    return connection.getBulkReply();
  }
