  @Override
  public String zrandmember(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrandmember(key));
  }
