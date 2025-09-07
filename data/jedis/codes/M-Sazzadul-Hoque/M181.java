  @Override
  @Deprecated
  public Class<?> jsonType(String key) {
    return executeCommand(commandObjects.jsonType(key));
  }
