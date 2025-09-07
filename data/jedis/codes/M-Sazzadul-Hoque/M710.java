  @Override
  public KeyValue<String, List<Tuple>> bzmpop(double timeout, SortedSetOption option, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bzmpop(timeout, option, keys));
  }
