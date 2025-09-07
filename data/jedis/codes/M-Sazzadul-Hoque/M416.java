  @Override
  public List<byte[]> aclLogBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, LOG);
    return connection.getBinaryMultiBulkReply();
  }
