  @Override
  public Set<String> keys(final String pattern) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.keys(pattern));
  }
