  @Override
  public Long memoryUsage(final String key, final int samples) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(MEMORY, USAGE.getRaw(), encode(key), SAMPLES.getRaw(), toByteArray(samples));
    return connection.getIntegerReply();
  }
