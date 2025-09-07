  @Override
  public String aclSetUser(String name, String... rules) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, joinParameters(SETUSER.name(), name, rules));
    return connection.getStatusCodeReply();
  }
