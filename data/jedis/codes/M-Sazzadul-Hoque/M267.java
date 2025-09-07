  public byte[] ping(final byte[] message) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.PING, message);
    return connection.getBinaryBulkReply();
  }
