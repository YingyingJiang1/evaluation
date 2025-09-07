  @Override
  public String ping(final String message) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.PING, message);
    return connection.getBulkReply();
  }
