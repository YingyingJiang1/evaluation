  @Override
  public Long objectFreq(final byte[] key) {
    connection.sendCommand(OBJECT, FREQ.getRaw(), key);
    return connection.getIntegerReply();
  }
