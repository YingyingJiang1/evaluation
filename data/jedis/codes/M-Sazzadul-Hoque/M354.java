  @Override
  public String configRewrite() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.CONFIG, Keyword.REWRITE);
    return connection.getStatusCodeReply();
  }
