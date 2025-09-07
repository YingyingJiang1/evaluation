  @Override
  public byte[] aclGenPassBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, GENPASS);
    return connection.getBinaryBulkReply();
  }
