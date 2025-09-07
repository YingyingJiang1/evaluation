  @Override
  public Long zrank(final String key, final String member) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrank(key, member));
  }
