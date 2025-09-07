  @Override
  public List<String> zrange(String key, ZRangeParams zRangeParams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrange(key, zRangeParams));
  }
