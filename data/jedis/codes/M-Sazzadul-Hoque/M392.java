  @Override
  public long bitcount(final byte[] key, final long start, final long end) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bitcount(key, start, end));
  }
