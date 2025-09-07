  @Override
  public Long memoryUsage(final String key) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(MEMORY, USAGE.name(), key);
    return connection.getIntegerReply();
  }
