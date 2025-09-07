  @Override
  public KeyValue<String, List<String>> blmpop(double timeout, ListDirection direction, int count, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blmpop(timeout, direction, count, keys));
  }
