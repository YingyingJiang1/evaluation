  @Override
  public List<byte[]> zdiff(final byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zdiff(keys));
  }
