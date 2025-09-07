  @Override
  public Tuple zpopmax(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zpopmax(key));
  }
