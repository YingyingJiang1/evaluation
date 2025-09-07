  @Override
  public String aclSetUser(byte[] name) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, SETUSER.getRaw(), name);
    return connection.getStatusCodeReply();
  }
