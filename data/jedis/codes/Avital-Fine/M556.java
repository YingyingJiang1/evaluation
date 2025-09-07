  @Override
  public long pexpireAt(final String key, final long millisecondsTimestamp) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpireAt(key, millisecondsTimestamp));
  }
