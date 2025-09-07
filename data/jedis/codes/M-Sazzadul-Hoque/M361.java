  @Override
  public byte[] echo(final byte[] string) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ECHO, string);
    return connection.getBinaryBulkReply();
  }
