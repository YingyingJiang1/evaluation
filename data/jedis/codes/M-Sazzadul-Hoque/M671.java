  @Override
  public KeyValue<String, String> brpop(final double timeout, final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.brpop(timeout, keys));
  }
