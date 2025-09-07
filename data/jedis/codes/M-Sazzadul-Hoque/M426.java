  @Override
  public byte[] clientInfoBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, Keyword.INFO);
    return connection.getBinaryBulkReply();
  }
