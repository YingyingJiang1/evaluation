  @Override
  public List<Map<String, String>> sentinelReplicas(String masterName) {
    connection.sendCommand(SENTINEL, REPLICAS.name(), masterName);
    return connection.getObjectMultiBulkReply().stream()
        .map(BuilderFactory.STRING_MAP::build).collect(Collectors.toList());
  }
