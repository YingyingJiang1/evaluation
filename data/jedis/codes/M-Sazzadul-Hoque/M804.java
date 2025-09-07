  @Override
  public List<Long> hexpire(String key, long seconds, ExpiryOption condition, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpire(key, seconds, condition, fields));
  }
