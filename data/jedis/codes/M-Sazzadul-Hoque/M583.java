  @Override
  public String hmset(final String key, final Map<String, String> hash) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hmset(key, hash));
  }
