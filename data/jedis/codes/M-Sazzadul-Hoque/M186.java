  @Override
  @Deprecated
  public Long jsonStrLen(String key, Path path) {
    return executeCommand(commandObjects.jsonStrLen(key, path));
  }
