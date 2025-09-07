  @Override
  public long zadd(final String key, final Map<String, Double> scoreMembers, final ZAddParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zadd(key, scoreMembers, params));
  }
