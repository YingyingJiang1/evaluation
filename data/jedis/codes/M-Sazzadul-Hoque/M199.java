  @Override
  @Deprecated
  public Long jsonArrTrim(String key, Path path, int start, int stop) {
    return executeCommand(commandObjects.jsonArrTrim(key, path, start, stop));
  }
