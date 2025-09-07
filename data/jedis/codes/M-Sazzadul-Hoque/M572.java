  @Override
  public long incr(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.incr(key));
  }
