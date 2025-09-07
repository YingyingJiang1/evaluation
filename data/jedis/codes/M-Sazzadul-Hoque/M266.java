  @Override
  public String ping() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.PING);
    return connection.getStatusCodeReply();
  }
