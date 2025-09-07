  @Override
  public byte[] aclGenPassBinary(int bits) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, GENPASS.getRaw(), toByteArray(bits));
    return connection.getBinaryBulkReply();
  }
