  @Override
  public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
    CommandArguments args = new CommandArguments(SENTINEL).add(SentinelKeyword.MONITOR)
        .add(masterName).add(ip).add(port).add(quorum);
    connection.sendCommand(args);
    return connection.getStatusCodeReply();
  }
