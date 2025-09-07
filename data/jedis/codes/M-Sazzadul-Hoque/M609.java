  @Override
  public List<String> rpop(final String key, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.rpop(key, count));
  }
