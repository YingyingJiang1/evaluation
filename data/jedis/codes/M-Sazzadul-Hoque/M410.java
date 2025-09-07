  @Override
  public AccessControlUser aclGetUser(byte[] name) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, GETUSER.getRaw(), name);
    return BuilderFactory.ACCESS_CONTROL_USER.build(connection.getObjectMultiBulkReply());
  }
