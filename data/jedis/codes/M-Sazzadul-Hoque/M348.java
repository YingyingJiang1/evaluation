  @Override
  @Deprecated
  public String slaveofNoOne() {
    connection.sendCommand(SLAVEOF, NO.getRaw(), ONE.getRaw());
    return connection.getStatusCodeReply();
  }
