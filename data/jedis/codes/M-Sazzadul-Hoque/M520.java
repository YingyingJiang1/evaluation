  @Override
  public List<Object> xautoclaim(byte[] key, byte[] groupName, byte[] consumerName,
      long minIdleTime, byte[] start, XAutoClaimParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xautoclaim(key, groupName, consumerName, minIdleTime, start, params));
  }
