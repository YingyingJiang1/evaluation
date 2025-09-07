  @Override
  public String mset(final String... keysvalues) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.mset(keysvalues));
  }
