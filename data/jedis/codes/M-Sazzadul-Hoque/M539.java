  @Override
  public long del(final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.del(keys));
  }
