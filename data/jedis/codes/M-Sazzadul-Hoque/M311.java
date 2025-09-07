  @Override
  public KeyValue<byte[], List<byte[]>> blmpop(double timeout, ListDirection direction, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blmpop(timeout, direction, keys));
  }
