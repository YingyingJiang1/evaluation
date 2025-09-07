  @Override
  public Long memoryUsage(final byte[] key, final int samples) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(MEMORY, USAGE.getRaw(), key, SAMPLES.getRaw(), toByteArray(samples));
    return connection.getIntegerReply();
  }
