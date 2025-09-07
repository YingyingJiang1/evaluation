  @Override
  public List<Long> hpexpire(String key, long milliseconds, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpire(key, milliseconds, fields));
  }
