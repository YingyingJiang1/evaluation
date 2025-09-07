  @Deprecated
  @Override
  public String getSet(final String key, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.getSet(key, value));
  }
