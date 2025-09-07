  @Override
  @Deprecated
  public String jsonSet(String key, Path path, Object pojo, JsonSetParams params) {
    return executeCommand(commandObjects.jsonSet(key, path, pojo, params));
  }
