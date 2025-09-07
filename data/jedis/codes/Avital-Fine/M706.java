  @Override
  public long zintercard(String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zintercard(keys));
  }
