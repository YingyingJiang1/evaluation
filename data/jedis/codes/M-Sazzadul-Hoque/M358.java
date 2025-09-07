  @Override
  public long strlen(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.strlen(key));
  }
