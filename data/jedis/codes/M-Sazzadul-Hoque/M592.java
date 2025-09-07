  @Override
  public Map<String, String> hgetAll(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hgetAll(key));
  }
