  @Override
  @Deprecated
  public Object jsonArrPop(String key, Path path, int index) {
    return executeCommand(commandObjects.jsonArrPop(key, path, index));
  }
