  @Override
  public List<Tuple> zunionWithScores(ZParams params, String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zunionWithScores(params, keys));
  }
