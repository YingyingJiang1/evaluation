  @Override
  public List<String> brpop(final int timeout, final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.brpop(timeout, key));
  }
