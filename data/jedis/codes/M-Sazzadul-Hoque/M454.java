  @Override
  public ScanResult<Map.Entry<byte[], byte[]>> hscan(final byte[] key, final byte[] cursor,
      final ScanParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hscan(key, cursor, params));
  }
