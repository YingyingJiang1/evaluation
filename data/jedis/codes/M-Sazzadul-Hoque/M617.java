  @Override
  public long scard(final String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.scard(key));
  }
