  @Override
  public long pexpire(final byte[] key, final long milliseconds, final ExpiryOption expiryOption) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpire(key, milliseconds, expiryOption));
  }
