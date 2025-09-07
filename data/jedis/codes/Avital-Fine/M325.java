  @Override
  public long zintercard(long limit, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zintercard(limit, keys));
  }
