  @Override
  public String replicaof(final String host, final int port) {
    connection.sendCommand(REPLICAOF, encode(host), toByteArray(port));
    return connection.getStatusCodeReply();
  }
