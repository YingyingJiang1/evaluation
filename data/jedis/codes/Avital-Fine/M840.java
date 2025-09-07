  @Override
  public Object fcall(final byte[] name, final List<byte[]> keys, final List<byte[]> args) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.fcall(name, keys, args));
  }
