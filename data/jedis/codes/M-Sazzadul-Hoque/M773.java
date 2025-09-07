  @Override
  public String clusterFailover() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER, ClusterKeyword.FAILOVER);
    return connection.getStatusCodeReply();
  }
