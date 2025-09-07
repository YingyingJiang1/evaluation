  @Override
  public List<byte[]> aclListBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, LIST);
    return connection.getBinaryMultiBulkReply();
  }
