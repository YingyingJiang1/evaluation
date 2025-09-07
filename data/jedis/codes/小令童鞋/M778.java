  @Override
  public String clusterAddSlotsRange(int... ranges) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER,
        joinParameters(ClusterKeyword.ADDSLOTSRANGE.getRaw(), joinParameters(ranges)));
    return connection.getStatusCodeReply();
  }
