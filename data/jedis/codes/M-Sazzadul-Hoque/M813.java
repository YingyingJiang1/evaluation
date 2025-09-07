  @Override
  public List<Long> httl(String key, String... fields) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.httl(key, fields));
  }
