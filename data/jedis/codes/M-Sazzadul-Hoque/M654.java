  @Override
  public List<String> zrandmember(final String key, final long count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrandmember(key, count));
  }
