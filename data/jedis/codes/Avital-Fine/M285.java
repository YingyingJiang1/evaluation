  @Override
  public long pexpireAt(final byte[] key, final long millisecondsTimestamp) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpireAt(key, millisecondsTimestamp));
  }
