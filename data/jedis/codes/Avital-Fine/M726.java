  @Override
  public long commandCount() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(COMMAND, COUNT);
    return connection.getIntegerReply();
  }
