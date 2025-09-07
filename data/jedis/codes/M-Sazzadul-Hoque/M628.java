  @Override
  public String srandmember(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.srandmember(key));
  }
