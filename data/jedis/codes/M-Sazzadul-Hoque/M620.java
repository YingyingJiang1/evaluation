  @Override
  public Set<String> sinter(final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sinter(keys));
  }
