  @Override
  public long xtrim(byte[] key, XTrimParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xtrim(key, params));
  }
