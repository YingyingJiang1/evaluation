  @Override
  public long clusterCountFailureReports(final String nodeId) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER, "COUNT-FAILURE-REPORTS",  nodeId);
    return connection.getIntegerReply();
  }
