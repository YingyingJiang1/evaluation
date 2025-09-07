  @Override
  public long pfadd(final byte[] key, final byte[]... elements) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pfadd(key, elements));
  }
