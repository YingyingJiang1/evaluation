  @Override
  public List<Long> hexpireAt(String key, long unixTimeSeconds, ExpiryOption condition, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpireAt(key, unixTimeSeconds, condition, fields));
  }
