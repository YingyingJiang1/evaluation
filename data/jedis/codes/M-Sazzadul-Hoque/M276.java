  @Override
  public long unlink(final byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.unlink(keys));
  }
