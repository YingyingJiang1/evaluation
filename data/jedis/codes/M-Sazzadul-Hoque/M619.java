  @Override
  public List<Boolean> smismember(final String key, final String... members) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.smismember(key, members));
  }
