  @Override
  public byte[] clientListBinary(ClientType type) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, LIST.getRaw(), type.getRaw());
    return connection.getBinaryBulkReply();
  }
