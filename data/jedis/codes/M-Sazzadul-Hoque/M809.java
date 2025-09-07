  @Override
  public List<Long> hpexpireAt(String key, long unixTimeMillis, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpireAt(key, unixTimeMillis, fields));
  }
