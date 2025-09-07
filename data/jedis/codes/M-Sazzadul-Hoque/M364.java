  @Override
  public boolean setbit(final byte[] key, final long offset, final boolean value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.setbit(key, offset, value));
  }
