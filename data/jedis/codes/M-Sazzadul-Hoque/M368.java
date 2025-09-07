  @Override
  public byte[] getrange(final byte[] key, final long startOffset, final long endOffset) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.getrange(key, startOffset, endOffset));
  }
