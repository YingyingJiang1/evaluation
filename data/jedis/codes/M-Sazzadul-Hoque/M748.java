  @Override
  public String aclSetUser(final String name) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, SETUSER.name(), name);
    return connection.getStatusCodeReply();
  }
