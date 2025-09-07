  @Override
  public long zadd(final String key, final Map<String, Double> scoreMembers) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zadd(key, scoreMembers));
  }
