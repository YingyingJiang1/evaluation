  @Override
  @Deprecated
  public Long jsonObjLen(String key) {
    return executeCommand(commandObjects.jsonObjLen(key));
  }
