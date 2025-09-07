  @Override
  public List<Double> zmscore(final String key, final String... members) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zmscore(key, members));
  }
