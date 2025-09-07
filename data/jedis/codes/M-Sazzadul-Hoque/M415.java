  @Override
  public List<byte[]> aclCat(byte[] category) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, CAT.getRaw(), category);
    return connection.getBinaryMultiBulkReply();
  }
