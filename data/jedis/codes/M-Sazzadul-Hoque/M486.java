  @Override
  public List<Long> bitfield(final byte[] key, final byte[]... arguments) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.bitfield(key, arguments));
  }
