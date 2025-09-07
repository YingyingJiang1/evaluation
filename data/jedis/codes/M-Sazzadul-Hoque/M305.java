  @Override
  public long sort(final byte[] key, final byte[] dstkey) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sort(key, dstkey));
  }
