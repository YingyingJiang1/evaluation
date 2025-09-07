  @Override
  @Deprecated
  public <T> T jsonArrPop(String key, Class<T> clazz, Path path, int index) {
    return executeCommand(commandObjects.jsonArrPop(key, clazz, path, index));
  }
