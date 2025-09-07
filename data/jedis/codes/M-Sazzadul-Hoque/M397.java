  @Override
  public long pttl(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pttl(key));
  }
