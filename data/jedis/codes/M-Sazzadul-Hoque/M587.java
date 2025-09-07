  @Override
  public boolean hexists(final String key, final String field) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexists(key, field));
  }
