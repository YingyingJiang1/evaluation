  @Override
  public KeyValue<String, Tuple> bzpopmax(double timeout, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bzpopmax(timeout, keys));
  }
