  @Override
  public String replicaofNoOne() {
    connection.sendCommand(REPLICAOF, NO.getRaw(), ONE.getRaw());
    return connection.getStatusCodeReply();
  }
