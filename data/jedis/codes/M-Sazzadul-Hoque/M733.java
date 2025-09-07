  @Override
  public String sentinelMyId() {
    connection.sendCommand(SENTINEL, MYID);
    return connection.getBulkReply();
  }
