  @Override
  public String hget(final String key, final String field) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hget(key, field));
  }
