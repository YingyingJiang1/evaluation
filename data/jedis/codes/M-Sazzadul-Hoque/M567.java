  @Override
  public long msetnx(final String... keysvalues) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.msetnx(keysvalues));
  }
