  @Override
  public long setrange(final byte[] key, final long offset, final byte[] value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.setrange(key, offset, value));
  }
