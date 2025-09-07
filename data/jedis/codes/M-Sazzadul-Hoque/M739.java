  @Override
  @Deprecated
  public List<Map<String, String>> sentinelSlaves(String masterName) {
    connection.sendCommand(SENTINEL, SLAVES.name(), masterName);
    return connection.getObjectMultiBulkReply().stream()
        .map(BuilderFactory.STRING_MAP::build).collect(Collectors.toList());
  }
