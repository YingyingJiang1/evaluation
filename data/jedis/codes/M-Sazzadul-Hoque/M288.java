  @Override
  public long touch(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.touch(key));
  }
