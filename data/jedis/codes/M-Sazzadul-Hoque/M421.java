  @Override
  public byte[] clientGetnameBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, GETNAME);
    return connection.getBinaryBulkReply();
  }
