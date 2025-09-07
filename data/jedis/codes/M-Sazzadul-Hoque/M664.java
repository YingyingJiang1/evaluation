  @Override
  public List<String> sort(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sort(key));
  }
