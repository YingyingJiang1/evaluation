  @Override
  public double incrByFloat(final String key, final double increment) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.incrByFloat(key, increment));
  }
