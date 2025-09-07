  @Override
  public LCSMatchResult lcs(final byte[] keyA, final byte[] keyB, final LCSParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lcs(keyA, keyB, params));
  }
