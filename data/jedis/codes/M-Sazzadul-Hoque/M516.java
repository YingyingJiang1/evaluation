  @Override
  public long xgroupDestroy(byte[] key, byte[] consumer) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xgroupDestroy(key, consumer));
  }
