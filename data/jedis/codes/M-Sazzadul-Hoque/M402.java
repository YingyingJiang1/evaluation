  @Override
  public String failover() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(Command.FAILOVER);
    connection.setTimeoutInfinite();
    try {
      return connection.getStatusCodeReply();
    } finally {
      connection.rollbackTimeout();
    }
  }
