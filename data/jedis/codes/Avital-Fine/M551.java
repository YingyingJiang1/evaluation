  @Override
  public long pexpire(final String key, final long milliseconds, final ExpiryOption expiryOption) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpire(key, milliseconds, expiryOption));
  }
