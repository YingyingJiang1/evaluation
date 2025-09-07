  @Override
  public double hincrByFloat(final String key, final String field, final double value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hincrByFloat(key, field, value));
  }
