  @Override
  public String failoverAbort() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.FAILOVER, ABORT);
    return connection.getStatusCodeReply();
  }
