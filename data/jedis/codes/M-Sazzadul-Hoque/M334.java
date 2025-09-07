  @Override
  public KeyValue<byte[], List<Tuple>> bzmpop(double timeout, SortedSetOption option, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bzmpop(timeout, option, keys));
  }
