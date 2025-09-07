  @Override
  public byte[] aclWhoAmIBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, WHOAMI);
    return connection.getBinaryBulkReply();
  }
