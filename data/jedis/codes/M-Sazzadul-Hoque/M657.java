  @Override
  public Double zscore(final String key, final String member) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zscore(key, member));
  }
