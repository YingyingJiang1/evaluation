  @Override
  @Deprecated
  public <T> List<T> jsonMGet(Path path, Class<T> clazz, String... keys) {
    return executeCommand(commandObjects.jsonMGet(path, clazz, keys));
  }
