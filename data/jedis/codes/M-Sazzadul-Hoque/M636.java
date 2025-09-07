  @Override
  public List<Tuple> zdiffWithScores(String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zdiffWithScores(keys));
  }
