  @Override
  public KeyValue<String, List<String>> lmpop(ListDirection direction, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lmpop(direction, keys));
  }
