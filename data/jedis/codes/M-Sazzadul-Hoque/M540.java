  @Override
  public long del(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.del(key));
  }
