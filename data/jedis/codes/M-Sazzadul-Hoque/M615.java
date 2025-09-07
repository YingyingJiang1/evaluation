  @Override
  public Set<String> spop(final String key, final long count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.spop(key, count));
  }
