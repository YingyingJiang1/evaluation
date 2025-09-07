  @Override
  public String setGet(final String key, final String value, final SetParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.setGet(key, value, params));
  }
