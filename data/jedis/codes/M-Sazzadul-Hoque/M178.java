  @Override
  @Deprecated
  public long jsonDel(String key, Path path) {
    return executeCommand(commandObjects.jsonDel(key, path));
  }
