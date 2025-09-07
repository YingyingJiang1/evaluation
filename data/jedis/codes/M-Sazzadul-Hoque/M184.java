  @Override
  @Deprecated
  public long jsonStrAppend(String key, Path path, Object string) {
    return executeCommand(commandObjects.jsonStrAppend(key, path, string));
  }
