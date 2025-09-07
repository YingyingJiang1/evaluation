  @Override
  public long exists(final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.exists(keys));
  }
