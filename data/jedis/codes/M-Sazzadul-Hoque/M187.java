  @Override
  @Deprecated
  public double jsonNumIncrBy(String key, Path path, double value) {
    return executeCommand(commandObjects.jsonNumIncrBy(key, path, value));
  }
