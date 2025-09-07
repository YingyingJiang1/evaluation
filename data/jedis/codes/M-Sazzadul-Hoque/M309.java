  @Override
  public KeyValue<byte[], List<byte[]>> lmpop(ListDirection direction, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lmpop(direction, keys));
  }
