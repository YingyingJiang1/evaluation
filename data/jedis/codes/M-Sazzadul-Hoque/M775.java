  @Override
  public String clusterMyId() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER, ClusterKeyword.MYID);
    return connection.getBulkReply();
  }
