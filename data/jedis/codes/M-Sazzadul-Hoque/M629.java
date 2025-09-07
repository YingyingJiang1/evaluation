  @Override
  public List<String> srandmember(final String key, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.srandmember(key, count));
  }
