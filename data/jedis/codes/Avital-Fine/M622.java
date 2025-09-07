  @Override
  public long sintercard(String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sintercard(keys));
  }
