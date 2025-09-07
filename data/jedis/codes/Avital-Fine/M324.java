  @Override
  public long zintercard(byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zintercard(keys));
  }
