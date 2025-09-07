  @Override
  public String save() {
    connection.sendCommand(Command.SAVE);
    return connection.getStatusCodeReply();
  }
