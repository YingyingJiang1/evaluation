  @Override
  public long lastsave() {
    connection.sendCommand(LASTSAVE);
    return connection.getIntegerReply();
  }
