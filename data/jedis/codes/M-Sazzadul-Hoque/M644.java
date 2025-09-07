  @Override
  public Long zrevrank(final String key, final String member) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrank(key, member));
  }
