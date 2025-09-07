  @Override
  public byte[] objectEncoding(final byte[] key) {
    connection.sendCommand(OBJECT, ENCODING.getRaw(), key);
    return connection.getBinaryBulkReply();
  }
