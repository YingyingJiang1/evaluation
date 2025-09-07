  @Override
  public boolean sismember(final String key, final String member) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sismember(key, member));
  }
