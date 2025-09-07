  @Override
  public String aclGenPass() {
    connection.sendCommand(ACL, GENPASS);
    return connection.getBulkReply();
  }
