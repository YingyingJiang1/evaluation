  @Override
  public List<byte[]> aclLogBinary(int limit) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, LOG.getRaw(), toByteArray(limit));
    return connection.getBinaryMultiBulkReply();
  }
