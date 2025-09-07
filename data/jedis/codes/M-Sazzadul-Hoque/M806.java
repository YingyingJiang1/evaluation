  @Override
  public List<Long> hpexpire(String key, long milliseconds, ExpiryOption condition, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpire(key, milliseconds, condition, fields));
  }
