  @Override
  public String clientNoEvictOn() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, "NO-EVICT", "ON");
    return connection.getBulkReply();
  }
