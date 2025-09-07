  @Override
  public long sort(final String key, final String dstkey) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sort(key, dstkey));
  }
