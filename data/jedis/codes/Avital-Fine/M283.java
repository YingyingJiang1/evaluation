  @Override
  public long pexpireTime(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pexpireTime(key));
  }
