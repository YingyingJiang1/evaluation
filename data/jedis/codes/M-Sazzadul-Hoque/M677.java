  @Override
  public KeyValue<String, Tuple> bzpopmin(double timeout, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bzpopmin(timeout, keys));
  }
