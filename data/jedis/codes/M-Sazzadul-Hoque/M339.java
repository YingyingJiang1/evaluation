  @Override
  public String bgrewriteaof() {
    connection.sendCommand(BGREWRITEAOF);
    return connection.getStatusCodeReply();
  }
