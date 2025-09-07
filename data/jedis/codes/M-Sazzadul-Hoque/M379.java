  @Override
  public String scriptFlush(final FlushMode flushMode) {
    connection.sendCommand(SCRIPT, FLUSH.getRaw(), flushMode.getRaw());
    return connection.getStatusCodeReply();
  }
