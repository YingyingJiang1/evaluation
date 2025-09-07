  @Override
  public List<String> aclUsers() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, USERS);
    return BuilderFactory.STRING_LIST.build(connection.getObjectMultiBulkReply());
  }
