  @Override
  public String restore(final byte[] key, final long ttl, final byte[] serializedValue) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.restore(key, ttl, serializedValue));
  }
