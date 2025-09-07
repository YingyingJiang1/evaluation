  @Override
  public String latencyDoctor() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(LATENCY, DOCTOR);
    return connection.getBulkReply();
  }
