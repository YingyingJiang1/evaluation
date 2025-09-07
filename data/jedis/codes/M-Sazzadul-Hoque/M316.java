  @Override
  public long zcount(final byte[] key, final byte[] min, final byte[] max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zcount(key, min, max));
  }
