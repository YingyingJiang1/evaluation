  @Override
  public List<byte[]> zrange(byte[] key, ZRangeParams zRangeParams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrange(key, zRangeParams));
  }
