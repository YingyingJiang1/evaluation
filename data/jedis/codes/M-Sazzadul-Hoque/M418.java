  @Override
  public String aclLogReset() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, LOG.getRaw(), Keyword.RESET.getRaw());
    return connection.getStatusCodeReply();
  }
