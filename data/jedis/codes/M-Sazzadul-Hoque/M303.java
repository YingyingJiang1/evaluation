  @Override
  public long zrangestore(byte[] dest, byte[] src, ZRangeParams zRangeParams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangestore(dest, src, zRangeParams));
  }
