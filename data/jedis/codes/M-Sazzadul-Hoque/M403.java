  @Override
  public String failover(FailoverParams failoverParams) {
    checkIsInMultiOrPipeline();
    CommandArguments args = new ClusterCommandArguments(Command.FAILOVER).addParams(failoverParams);
    connection.sendCommand(args);
    connection.setTimeoutInfinite();
    try {
      return connection.getStatusCodeReply();
    } finally {
      connection.rollbackTimeout();
    }
  }
