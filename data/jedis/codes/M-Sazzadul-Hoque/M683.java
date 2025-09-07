  @Override
  public long zcount(final String key, final String min, final String max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zcount(key, min, max));
  }
