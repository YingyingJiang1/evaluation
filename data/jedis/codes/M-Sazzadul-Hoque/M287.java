  @Override
  public long touch(final byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.touch(keys));
  }
