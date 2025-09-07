  @Override
  public String clientNoEvictOff() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, "NO-EVICT", "OFF");
    return connection.getBulkReply();
  }
