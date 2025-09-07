  @Override
  @Deprecated
  public <T> T jsonArrPop(String key, Class<T> clazz, Path path) {
    return executeCommand(commandObjects.jsonArrPop(key, clazz, path));
  }
