  @Override
  public long expire(final String key, final long seconds) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.expire(key, seconds));
  }
