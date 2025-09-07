  @Override
  public byte[] clientListBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, LIST);
    return connection.getBinaryBulkReply();
  }
