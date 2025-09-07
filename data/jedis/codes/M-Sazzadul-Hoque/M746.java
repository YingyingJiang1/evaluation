  @Override
  public String restore(final String key, final long ttl, final byte[] serializedValue,
      final RestoreParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.restore(key, ttl, serializedValue, params));
  }
