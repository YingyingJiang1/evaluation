  @Override
  @Deprecated
  public String jsonGetAsPlainString(String key, Path path) {
    return executeCommand(commandObjects.jsonGetAsPlainString(key, path));
  }
