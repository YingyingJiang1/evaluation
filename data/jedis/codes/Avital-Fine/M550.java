  @Override
  public long pexpire(final String key, final long milliseconds) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpire(key, milliseconds));
  }
