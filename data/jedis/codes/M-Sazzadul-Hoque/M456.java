  @Override
  public ScanResult<byte[]> sscan(final byte[] key, final byte[] cursor, final ScanParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sscan(key, cursor, params));
  }
