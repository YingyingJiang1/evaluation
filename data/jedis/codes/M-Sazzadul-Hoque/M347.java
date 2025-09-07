  @Override
  @Deprecated
  public String slaveof(final String host, final int port) {
    connection.sendCommand(SLAVEOF, encode(host), toByteArray(port));
    return connection.getStatusCodeReply();
  }
