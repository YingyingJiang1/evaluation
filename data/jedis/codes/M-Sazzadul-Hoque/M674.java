  @Override
  public KeyValue<String, List<String>> blmpop(double timeout, ListDirection direction, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blmpop(timeout, direction, keys));
  }
