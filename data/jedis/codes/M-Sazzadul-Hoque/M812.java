  @Override
  public List<Long> hpexpireTime(String key, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hpexpireTime(key, fields));
  }
