  @Override
  public String info() {
    connection.sendCommand(Command.INFO);
    return connection.getBulkReply();
  }
