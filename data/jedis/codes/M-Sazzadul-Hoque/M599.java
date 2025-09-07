  @Override
  public String ltrim(final String key, final long start, final long stop) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.ltrim(key, start, stop));
  }
