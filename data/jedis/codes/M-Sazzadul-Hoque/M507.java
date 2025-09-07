  @Override
  public byte[] xadd(final byte[] key, final XAddParams params, final Map<byte[], byte[]> hash) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xadd(key, params, hash));
  }
