  @Override
  public long zintercard(long limit, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zintercard(limit, keys));
  }
