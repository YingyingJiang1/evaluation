  @Override
  public long hset(final String key, final Map<String, String> hash) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hset(key, hash));
  }
