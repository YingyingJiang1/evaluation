  @Override
  public String xgroupCreate(byte[] key, byte[] consumer, byte[] id, boolean makeStream) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xgroupCreate(key, consumer, id, makeStream));
  }
