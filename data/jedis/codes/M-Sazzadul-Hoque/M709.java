  @Override
  public KeyValue<String, List<Tuple>> zmpop(SortedSetOption option, int count, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zmpop(option, count, keys));
  }
