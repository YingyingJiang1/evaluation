  @Override
  public Long objectRefcount(final byte[] key) {
    connection.sendCommand(OBJECT, REFCOUNT.getRaw(), key);
    return connection.getIntegerReply();
  }
