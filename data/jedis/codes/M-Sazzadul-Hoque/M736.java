  @Override
  public List<Map<String, String>> sentinelSentinels(String masterName) {
    connection.sendCommand(SENTINEL, SENTINELS.name(), masterName);
    return connection.getObjectMultiBulkReply().stream()
        .map(BuilderFactory.STRING_MAP::build).collect(Collectors.toList());
  }
