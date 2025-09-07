  @Override
  public byte[] setGet(final byte[] key, final byte[] value, final SetParams params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.setGet(key, value, params));
  }
