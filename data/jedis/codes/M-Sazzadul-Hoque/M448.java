  @Override
  public long pfcount(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pfcount(key));
  }
