  @Override
  public List<Long> hexpireAt(String key, long unixTimeSeconds, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpireAt(key, unixTimeSeconds, fields));
  }
