  @Override
  public long unlink(final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.unlink(keys));
  }
