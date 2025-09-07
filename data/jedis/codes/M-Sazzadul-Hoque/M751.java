  @Override
  public List<String> aclList() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, LIST);
    return connection.getMultiBulkReply();
  }
