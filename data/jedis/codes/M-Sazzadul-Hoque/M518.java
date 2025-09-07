  @Override
  public long xtrim(byte[] key, long maxLen, boolean approximateLength) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xtrim(key, maxLen, approximateLength));
  }
