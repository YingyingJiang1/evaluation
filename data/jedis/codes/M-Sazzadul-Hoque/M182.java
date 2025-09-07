  @Override
  @Deprecated
  public Class<?> jsonType(String key, Path path) {
    return executeCommand(commandObjects.jsonType(key, path));
  }
