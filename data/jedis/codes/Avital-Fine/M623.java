  @Override
  public long sintercard(int limit, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sintercard(limit, keys));
  }
