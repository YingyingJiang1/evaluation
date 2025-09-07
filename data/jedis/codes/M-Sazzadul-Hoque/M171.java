  @Override
  @Deprecated
  public String jsonSetWithPlainString(String key, Path path, String string) {
    return executeCommand(commandObjects.jsonSetWithPlainString(key, path, string));
  }
