  @Override
  public List<Map<String, String>> sentinelMasters() {
    connection.sendCommand(SENTINEL, MASTERS);
    return connection.getObjectMultiBulkReply().stream()
        .map(BuilderFactory.STRING_MAP::build).collect(Collectors.toList());
  }
