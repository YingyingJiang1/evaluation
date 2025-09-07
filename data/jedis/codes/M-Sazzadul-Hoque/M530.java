  @Override
  public String set(final String key, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.set(key, value));
  }
