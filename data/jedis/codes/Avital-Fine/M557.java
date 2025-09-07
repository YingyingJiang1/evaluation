  @Override
  public long pexpireAt(String key, long millisecondsTimestamp, ExpiryOption expiryOption) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpireAt(key, millisecondsTimestamp, expiryOption));
  }
