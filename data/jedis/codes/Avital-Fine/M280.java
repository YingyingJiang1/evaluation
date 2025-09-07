  @Override
  public long pexpire(final byte[] key, final long milliseconds) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpire(key, milliseconds));
  }
