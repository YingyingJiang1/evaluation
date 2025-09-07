  @Override
  public Long lpos(final String key, final String element, final LPosParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lpos(key, element, params));
  }
