  @Override
  public String clientList(ClientType type) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, LIST.getRaw(), Keyword.TYPE.getRaw(), type.getRaw());
    return connection.getBulkReply();
  }
