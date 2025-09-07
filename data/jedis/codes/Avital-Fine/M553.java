  @Override
  public long pexpireTime(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpireTime(key));
  }
