  @Override
  public String setex(final String key, final long seconds, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.setex(key, seconds, value));
  }
