  @Override
  public boolean getbit(final byte[] key, final long offset) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.getbit(key, offset));
  }
