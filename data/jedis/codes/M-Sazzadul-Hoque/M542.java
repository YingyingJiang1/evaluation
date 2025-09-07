  @Override
  public long unlink(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.unlink(key));
  }
