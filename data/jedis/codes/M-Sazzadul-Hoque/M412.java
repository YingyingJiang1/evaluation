  @Override
  public String aclSetUser(byte[] name, byte[]... rules) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, joinParameters(SETUSER.getRaw(), name, rules));
    return connection.getStatusCodeReply();
  }
