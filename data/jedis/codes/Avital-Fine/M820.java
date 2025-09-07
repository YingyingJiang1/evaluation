  @Override
  public String lolwut(LolwutParams lolwutParams) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(new CommandArguments(LOLWUT).addParams(lolwutParams));
    return connection.getBulkReply();
  }
