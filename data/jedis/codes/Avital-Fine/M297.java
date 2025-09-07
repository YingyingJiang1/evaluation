  @Override
  public long sintercard(byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sintercard(keys));
  }
