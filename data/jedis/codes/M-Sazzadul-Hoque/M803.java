  @Override
  public List<Long> hexpire(String key, long seconds, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpire(key, seconds, fields));
  }
