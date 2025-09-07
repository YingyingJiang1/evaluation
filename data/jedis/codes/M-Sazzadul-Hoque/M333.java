  @Override
  public KeyValue<byte[], List<Tuple>> zmpop(SortedSetOption option, int count, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zmpop(option, count, keys));
  }
