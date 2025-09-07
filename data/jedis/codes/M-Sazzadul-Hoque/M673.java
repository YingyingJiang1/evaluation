  @Override
  public KeyValue<String, List<String>> lmpop(ListDirection direction, int count, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lmpop(direction, count, keys));
  }
