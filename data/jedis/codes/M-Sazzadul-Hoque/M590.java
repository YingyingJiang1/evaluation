  @Override
  public Set<String> hkeys(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hkeys(key));
  }
