  @Override
  public String lolwut() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(LOLWUT);
    return connection.getBulkReply();
  }
