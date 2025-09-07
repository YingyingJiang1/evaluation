  @Override
  public long bitcount(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bitcount(key));
  }
