  @Override
  public long hdel(final String key, final String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hdel(key, fields));
  }
