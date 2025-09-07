  @Override
  public ScanResult<byte[]> scan(final byte[] cursor, final ScanParams params, final byte[] type) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.scan(cursor, params, type));
  }
