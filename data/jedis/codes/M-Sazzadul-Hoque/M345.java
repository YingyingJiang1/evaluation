  @Override
  public String info(final String section) {
    connection.sendCommand(Command.INFO, section);
    return connection.getBulkReply();
  }
