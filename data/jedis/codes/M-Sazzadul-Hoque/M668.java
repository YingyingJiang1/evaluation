  @Override
  public List<String> blpop(final int timeout, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blpop(timeout, keys));
  }
