  @Override
  @Deprecated
  public long jsonDebugMemory(String key, Path path) {
    return executeCommand(commandObjects.jsonDebugMemory(key, path));
  }
