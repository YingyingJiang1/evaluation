  @Override
  public String psetex(final String key, final long milliseconds, final String value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.psetex(key, milliseconds, value));
  }
