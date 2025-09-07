  @Override
  public String sentinelFailover(String masterName) {
    connection.sendCommand(SENTINEL, SentinelKeyword.FAILOVER.name(), masterName);
    return connection.getStatusCodeReply();
  }
