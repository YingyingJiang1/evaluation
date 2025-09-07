  @Override
  public List<Long> hpexpireAt(String key, long unixTimeMillis, ExpiryOption condition, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpireAt(key, unixTimeMillis, condition, fields));
  }
