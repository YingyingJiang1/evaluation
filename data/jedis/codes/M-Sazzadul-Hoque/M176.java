  @Override
  @Deprecated
  public <T> T jsonGet(String key, Class<T> clazz, Path... paths) {
    return executeCommand(commandObjects.jsonGet(key, clazz, paths));
  }
