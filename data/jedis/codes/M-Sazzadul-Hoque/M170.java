  @Override
  @Deprecated
  public String jsonSet(String key, Path path, Object pojo) {
    return executeCommand(commandObjects.jsonSet(key, path, pojo));
  }
