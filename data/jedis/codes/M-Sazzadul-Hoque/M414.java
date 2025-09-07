  @Override
  public List<byte[]> aclCatBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, CAT);
    return connection.getBinaryMultiBulkReply();
  }
