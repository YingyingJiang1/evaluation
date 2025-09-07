  @Override
  public List<String> hmget(final String key, final String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hmget(key, fields));
  }
