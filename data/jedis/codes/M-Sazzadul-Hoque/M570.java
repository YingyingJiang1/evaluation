  @Override
  public long incrBy(final String key, final long increment) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.incrBy(key, increment));
  }
