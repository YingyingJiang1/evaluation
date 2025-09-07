  @Override
  public List<Object> xautoclaimJustId(byte[] key, byte[] groupName, byte[] consumerName,
      long minIdleTime, byte[] start, XAutoClaimParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xautoclaimJustId(key, groupName, consumerName, minIdleTime, start, params));
  }
