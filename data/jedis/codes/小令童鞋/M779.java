  @Override
  public String clusterDelSlotsRange(int... ranges) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER,
        joinParameters(ClusterKeyword.DELSLOTSRANGE.getRaw(), joinParameters(ranges)));
    return connection.getStatusCodeReply();
  }
