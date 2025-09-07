  @Override
  public String lset(final String key, final long index, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lset(key, index, value));
  }
