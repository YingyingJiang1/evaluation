  @Override
  public long touch(final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.touch(keys));
  }
