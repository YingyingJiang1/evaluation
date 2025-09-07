  @Override
  @Deprecated
  public Long jsonObjLen(String key, Path path) {
    return executeCommand(commandObjects.jsonObjLen(key, path));
  }
