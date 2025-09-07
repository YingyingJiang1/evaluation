  @Override
  public List<byte[]> zinter(final ZParams params, final byte[]... keys) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zinter(params, keys));
  }
