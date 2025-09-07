  @Override
  public List<String> blpop(final int timeout, final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blpop(timeout, key));
  }
