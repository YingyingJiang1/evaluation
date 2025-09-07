  @Override
  public List<Tuple> zpopmax(final String key, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zpopmax(key, count));
  }
