  @Override
  public KeyValue<byte[], List<Tuple>> zmpop(SortedSetOption option, byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zmpop(option, keys));
  }
