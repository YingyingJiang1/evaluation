  @Override
  public String clusterBumpEpoch() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER, ClusterKeyword.BUMPEPOCH);
    return connection.getBulkReply();
  }
