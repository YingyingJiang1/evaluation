  @Override
  public String configResetStat() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.CONFIG, Keyword.RESETSTAT);
    return connection.getStatusCodeReply();
  }
