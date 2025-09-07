  @Override
  public ScanResult<byte[]> scan(final byte[] cursor, final ScanParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.scan(cursor, params));
  }
