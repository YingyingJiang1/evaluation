  @Override
  public long clientKill(ClientKillParams params) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(new CommandArguments(CLIENT).add(KILL).addParams(params));
    return this.connection.getIntegerReply();
  }
