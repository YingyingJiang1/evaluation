  @Override
  public List<Object> slowlogGetBinary(final long entries) {
    connection.sendCommand(SLOWLOG, Keyword.GET.getRaw(), toByteArray(entries));
    return connection.getObjectMultiBulkReply();
  }
