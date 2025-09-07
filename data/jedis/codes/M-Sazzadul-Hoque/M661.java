  @Override
  public Tuple zpopmin(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zpopmin(key));
  }
