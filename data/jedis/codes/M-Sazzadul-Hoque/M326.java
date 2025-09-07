  @Override
  public long zlexcount(final byte[] key, final byte[] min, final byte[] max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zlexcount(key, min, max));
  }
