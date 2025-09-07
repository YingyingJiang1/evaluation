  @Override
  public List<String> lpop(final String key, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lpop(key, count));
  }
