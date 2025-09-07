  @Override
  public long unlink(final byte[] key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.unlink(key));
  }
