  @Override
  @Deprecated
  public long jsonArrIndex(String key, Path path, Object scalar) {
    return executeCommand(commandObjects.jsonArrIndex(key, path, scalar));
  }
