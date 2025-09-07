  @Override
  public String setGet(final String key, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.setGet(key, value));
  }
