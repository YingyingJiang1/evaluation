  @Override
  public Set<String> sdiff(final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sdiff(keys));
  }
