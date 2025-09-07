  @Override
  public long decrBy(final String key, final long decrement) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.decrBy(key, decrement));
  }
