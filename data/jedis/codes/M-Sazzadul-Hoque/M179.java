  @Override
  @Deprecated
  public long jsonClear(String key, Path path) {
    return executeCommand(commandObjects.jsonClear(key, path));
  }
