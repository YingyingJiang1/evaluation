  @Override
  public Long lpos(final String key, final String element) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lpos(key, element));
  }
