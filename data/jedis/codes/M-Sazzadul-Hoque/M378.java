  @Override
  public String scriptFlush() {
    connection.sendCommand(SCRIPT, FLUSH);
    return connection.getStatusCodeReply();
  }
