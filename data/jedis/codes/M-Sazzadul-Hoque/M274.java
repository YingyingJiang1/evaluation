  @Override
  public byte[] setGet(final byte[] key, final byte[] value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.setGet(key, value));
  }
