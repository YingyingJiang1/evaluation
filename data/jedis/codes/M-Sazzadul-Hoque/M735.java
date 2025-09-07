  @Override
  public Map<String, String> sentinelMaster(String masterName) {
    connection.sendCommand(SENTINEL, MASTER.name(), masterName);
    return BuilderFactory.STRING_MAP.build(connection.getOne());
  }
