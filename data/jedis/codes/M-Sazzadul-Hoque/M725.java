  @Override
  public long bitcount(final String key, final long start, final long end, final BitCountOption option) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bitcount(key, start, end, option));
  }
