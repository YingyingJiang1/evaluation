  @Override
  public List<Long> hpttl(String key, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpttl(key, fields));
  }
