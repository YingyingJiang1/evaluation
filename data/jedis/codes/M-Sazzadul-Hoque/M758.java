  @Override
  public String clientGetname() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, GETNAME);
    return connection.getBulkReply();
  }
