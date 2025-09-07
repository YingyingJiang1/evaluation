  @Override
  public long pfcount(final byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.pfcount(keys));
  }
