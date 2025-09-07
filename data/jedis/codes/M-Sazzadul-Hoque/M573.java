  @Override
  public long append(final String key, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.append(key, value));
  }
