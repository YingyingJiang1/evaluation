  @Override
  public long zremrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zremrangeByLex(key, min, max));
  }
