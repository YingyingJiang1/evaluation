  @Override
  public String substr(final String key, final int start, final int end) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.substr(key, start, end));
  }
