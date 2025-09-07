  @Override
  public byte[] clientListBinary(final long... clientIds) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLIENT, clientListParams(clientIds));
    return connection.getBinaryBulkReply();
  }
