  @Override
  public ScanResult<byte[]> scan(final byte[] cursor) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.scan(cursor));
  }
