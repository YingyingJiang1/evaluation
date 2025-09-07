  @Override
  @Deprecated
  public <T> T jsonGet(String key, Class<T> clazz) {
    return executeCommand(commandObjects.jsonGet(key, clazz));
  }
