  @Override
  public LCSMatchResult lcs(final String keyA, final String keyB, final LCSParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lcs(keyA, keyB, params));
  }
