  @Override
  public Double zincrby(final String key, final double increment, final String member,
      final ZIncrByParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zincrby(key, increment, member, params));
  }
