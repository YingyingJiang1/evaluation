  @Override
  public Long objectIdletime(final byte[] key) {
    connection.sendCommand(OBJECT, IDLETIME.getRaw(), key);
    return connection.getIntegerReply();
  }
