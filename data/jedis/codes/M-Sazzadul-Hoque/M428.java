  @Override
  public String clientSetname(final byte[] name) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, SETNAME.getRaw(), name);
    return connection.getBulkReply();
  }
