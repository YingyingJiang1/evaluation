  @Override
  public String xgroupSetID(byte[] key, byte[] consumer, byte[] id) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xgroupSetID(key, consumer, id));
  }
