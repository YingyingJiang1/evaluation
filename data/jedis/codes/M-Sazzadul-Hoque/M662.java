  @Override
  public List<Tuple> zpopmin(final String key, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zpopmin(key, count));
  }
