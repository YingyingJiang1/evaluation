  @Override
  public String lindex(final String key, final long index) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lindex(key, index));
  }
