  @Override
  public String clusterMyShardId() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER, ClusterKeyword.MYSHARDID);
    return connection.getBulkReply();
  }
