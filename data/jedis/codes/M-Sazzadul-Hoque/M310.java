  @Override
  public KeyValue<byte[], List<byte[]>> lmpop(ListDirection direction, int count, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lmpop(direction, count, keys));
  }
