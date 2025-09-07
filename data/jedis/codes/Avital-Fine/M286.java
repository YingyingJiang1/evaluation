  @Override
  public long pexpireAt(byte[] key, long millisecondsTimestamp, ExpiryOption expiryOption) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpireAt(key, millisecondsTimestamp, expiryOption));
  }
