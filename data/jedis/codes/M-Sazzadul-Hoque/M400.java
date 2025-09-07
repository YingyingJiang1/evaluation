  @Override
  public Long memoryUsage(final byte[] key) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(MEMORY, USAGE.getRaw(), key);
    return connection.getIntegerReply();
  }
