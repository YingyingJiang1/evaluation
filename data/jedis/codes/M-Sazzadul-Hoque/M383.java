  @Override
  public long slowlogLen() {
    connection.sendCommand(SLOWLOG, LEN);
    return connection.getIntegerReply();
  }
