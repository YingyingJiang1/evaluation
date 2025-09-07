  @Override
  @Deprecated
  public Long jsonArrLen(String key, Path path) {
    return executeCommand(commandObjects.jsonArrLen(key, path));
  }
