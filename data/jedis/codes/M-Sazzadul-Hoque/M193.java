  @Override
  @Deprecated
  public Object jsonArrPop(String key, Path path) {
    return executeCommand(commandObjects.jsonArrPop(key, path));
  }
