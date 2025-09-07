  @Override
  public byte[] brpoplpush(final byte[] source, final byte[] destination, final int timeout) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.brpoplpush(source, destination, timeout));
  }
