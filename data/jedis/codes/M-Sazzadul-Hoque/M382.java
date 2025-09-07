  @Override
  public byte[] scriptLoad(final byte[] script) {
    connection.sendCommand(SCRIPT, LOAD.getRaw(), script);
    return connection.getBinaryBulkReply();
  }
