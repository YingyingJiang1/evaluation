  @Override
  public List<Long> hexpireTime(String key, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hexpireTime(key, fields));
  }
