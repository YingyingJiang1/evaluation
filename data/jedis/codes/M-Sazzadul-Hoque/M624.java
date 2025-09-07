  @Override
  public Set<String> sunion(final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sunion(keys));
  }
