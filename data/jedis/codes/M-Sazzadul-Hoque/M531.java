  @Override
  public String set(final String key, final String value, final SetParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.set(key, value, params));
  }
