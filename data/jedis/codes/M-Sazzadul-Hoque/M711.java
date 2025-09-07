  @Override
  public KeyValue<String, List<Tuple>> bzmpop(double timeout, SortedSetOption option, int count, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bzmpop(timeout, option, count, keys));
  }
