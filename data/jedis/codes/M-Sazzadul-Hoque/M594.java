  @Override
  public List<String> hrandfield(final String key, final long count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hrandfield(key, count));
  }
