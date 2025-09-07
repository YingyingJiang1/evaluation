  @Override
  public String auth(final String user, final String password) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.AUTH, user, password);
    return connection.getStatusCodeReply();
  }
