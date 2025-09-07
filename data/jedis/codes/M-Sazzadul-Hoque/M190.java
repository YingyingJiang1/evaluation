  @Override
  @Deprecated
  public long jsonArrInsert(String key, Path path, int index, Object... pojos) {
    return executeCommand(commandObjects.jsonArrInsert(key, path, index, pojos));
  }
