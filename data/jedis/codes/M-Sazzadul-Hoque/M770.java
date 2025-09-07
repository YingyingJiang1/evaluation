  @Override
  public String clusterSetConfigEpoch(long configEpoch) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER, "SET-CONFIG-EPOCH", Long.toString(configEpoch));
    return connection.getStatusCodeReply();
  }
