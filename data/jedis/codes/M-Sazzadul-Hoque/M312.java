  @Override
  public KeyValue<byte[], List<byte[]>> blmpop(double timeout, ListDirection direction, int count, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.blmpop(timeout, direction, count, keys));
  }
