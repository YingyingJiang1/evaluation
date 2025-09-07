  @Override
  @Deprecated
  public Object jsonGet(String key, Path... paths) {
    return executeCommand(commandObjects.jsonGet(key, paths));
  }
