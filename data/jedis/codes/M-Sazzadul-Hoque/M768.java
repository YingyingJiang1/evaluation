  @Override
  public String clusterReset(final ClusterResetType resetType) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER, ClusterKeyword.RESET.getRaw(), resetType.getRaw());
    return connection.getStatusCodeReply();
  }
