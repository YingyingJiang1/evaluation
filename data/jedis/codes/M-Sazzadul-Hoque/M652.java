  @Override
  public long zrangestore(String dest, String src, ZRangeParams zRangeParams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangestore(dest, src, zRangeParams));
  }
