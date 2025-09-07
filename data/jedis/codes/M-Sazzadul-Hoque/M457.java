  @Override
  public ScanResult<Tuple> zscan(final byte[] key, final byte[] cursor, final ScanParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zscan(key, cursor, params));
  }
