  @Override
  public Set<String> smembers(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.smembers(key));
  }
