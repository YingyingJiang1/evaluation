  @Override
  @Deprecated
  public long jsonStrAppend(String key, Object string) {
    return executeCommand(commandObjects.jsonStrAppend(key, string));
  }
