  @Override
  public List<Map<String, Object>> clusterLinks() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(CLUSTER, ClusterKeyword.LINKS);
    return connection.getObjectMultiBulkReply().stream()
            .map(BuilderFactory.ENCODED_OBJECT_MAP::build).collect(Collectors.toList());
  }
