  @Override
  public KeyValue<byte[], List<Tuple>> bzmpop(double timeout, SortedSetOption option, int count, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bzmpop(timeout, option, count, keys));
  }
