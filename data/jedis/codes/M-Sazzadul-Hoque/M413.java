  @Override
  public long aclDelUser(byte[]... names) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, joinParameters(DELUSER.getRaw(), names));
    return connection.getIntegerReply();
  }
