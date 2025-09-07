  @Override
  public KeyValue<String, String> blpop(final double timeout, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blpop(timeout, keys));
  }
