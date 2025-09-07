  @Override
  @Deprecated
  public <T> T jsonArrPop(String key, Class<T> clazz) {
    return executeCommand(commandObjects.jsonArrPop(key, clazz));
  }
