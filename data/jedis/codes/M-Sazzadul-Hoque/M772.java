  @Override
  public List<String> clusterReplicas(final String nodeId) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER, ClusterKeyword.REPLICAS.name(), nodeId);
    return connection.getMultiBulkReply();
  }
