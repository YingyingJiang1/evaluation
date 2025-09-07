  @Override
  @Deprecated
  public Object jsonArrPop(String key) {
    return executeCommand(commandObjects.jsonArrPop(key));
  }
