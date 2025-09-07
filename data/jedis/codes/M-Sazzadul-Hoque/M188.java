  @Override
  @Deprecated
  public Long jsonArrAppend(String key, Path path, Object... pojos) {
    return executeCommand(commandObjects.jsonArrAppend(key, path, pojos));
  }
