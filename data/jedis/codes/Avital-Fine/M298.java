  @Override
  public long sintercard(int limit, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sintercard(limit, keys));
  }
