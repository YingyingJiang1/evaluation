  @Override
  public List<byte[]> aclUsersBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, USERS);
    return connection.getBinaryMultiBulkReply();
  }
