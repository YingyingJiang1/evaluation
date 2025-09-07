  @Override
  public String flushDB(FlushMode flushMode) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(FLUSHDB, flushMode.getRaw());
    return connection.getStatusCodeReply();
  }
