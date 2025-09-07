  @Override
  public long clientId() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, ID);
    return connection.getIntegerReply();
  }
