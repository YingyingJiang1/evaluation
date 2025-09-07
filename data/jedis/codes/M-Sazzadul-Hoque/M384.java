  @Override
  public List<Object> slowlogGetBinary() {
    connection.sendCommand(SLOWLOG, Keyword.GET);
    return connection.getObjectMultiBulkReply();
  }
