  @Override
  public long ttl(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.ttl(key));
  }
