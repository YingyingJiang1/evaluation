  @Override
  public String flushAll(FlushMode flushMode) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(FLUSHALL, flushMode.getRaw());
    return connection.getStatusCodeReply();
  }
