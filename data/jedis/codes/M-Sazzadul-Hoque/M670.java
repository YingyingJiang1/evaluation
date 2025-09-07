  @Override
  public List<String> brpop(final int timeout, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.brpop(timeout, keys));
  }
