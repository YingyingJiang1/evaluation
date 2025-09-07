  @Override
  @Deprecated
  public Long jsonArrLen(String key) {
    return executeCommand(commandObjects.jsonArrLen(key));
  }
