  @Override
  public String auth(final String password) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.AUTH, password);
    return connection.getStatusCodeReply();
  }
