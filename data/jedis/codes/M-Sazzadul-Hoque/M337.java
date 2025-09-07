  @Override
  public String bgsave() {
    connection.sendCommand(BGSAVE);
    return connection.getStatusCodeReply();
  }
