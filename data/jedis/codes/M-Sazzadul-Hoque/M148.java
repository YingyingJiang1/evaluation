  @Deprecated
  @Override
  public String getSet(String key, String value) {
    return executeCommand(commandObjects.getSet(key, value));
  }
