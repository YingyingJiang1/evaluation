  @Override
  public List<String> mget(final String... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.mget(keys));
  }
