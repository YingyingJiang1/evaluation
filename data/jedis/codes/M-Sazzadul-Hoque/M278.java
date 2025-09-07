  @Override
  public long dbSize() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(DBSIZE);
    return connection.getIntegerReply();
  }
