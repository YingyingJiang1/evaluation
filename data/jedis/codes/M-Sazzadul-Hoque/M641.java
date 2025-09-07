  @Override
  public double zincrby(final String key, final double increment, final String member) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zincrby(key, increment, member));
  }
