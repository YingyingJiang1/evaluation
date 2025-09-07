  @Override
  public long hset(final byte[] key, final Map<byte[], byte[]> hash) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hset(key, hash));
  }
