  @Override
  public KeyValue<String, List<Tuple>> zmpop(SortedSetOption option, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zmpop(option, keys));
  }
