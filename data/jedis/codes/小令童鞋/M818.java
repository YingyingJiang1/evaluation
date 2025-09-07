  @Override
  public Map<String, Object> memoryStats() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(MEMORY, STATS);
    return BuilderFactory.ENCODED_OBJECT_MAP.build(connection.getOne());
  }
