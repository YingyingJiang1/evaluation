  @Override
  @Deprecated
  public Long jsonStrLen(String key) {
    return executeCommand(commandObjects.jsonStrLen(key));
  }
