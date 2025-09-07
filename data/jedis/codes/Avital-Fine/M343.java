  @Override
  public String shutdownAbort() {
    connection.sendCommand(SHUTDOWN, ABORT);
    return connection.getStatusCodeReply();
  }
