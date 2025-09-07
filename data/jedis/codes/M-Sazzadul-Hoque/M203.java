  @Override
  @Deprecated
  public List<String> jsonObjKeys(String key, Path path) {
    return executeCommand(commandObjects.jsonObjKeys(key, path));
  }
