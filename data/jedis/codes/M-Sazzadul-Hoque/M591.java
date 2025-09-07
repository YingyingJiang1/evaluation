  @Override
  public List<String> hvals(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hvals(key));
  }
