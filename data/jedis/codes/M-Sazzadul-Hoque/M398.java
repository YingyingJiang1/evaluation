  @Override
  public String psetex(final byte[] key, final long milliseconds, final byte[] value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.psetex(key, milliseconds, value));
  }
