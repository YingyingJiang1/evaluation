  @Override
  public Object fcallReadonly(final byte[] name, final List<byte[]> keys, final List<byte[]> args) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.fcallReadonly(name, keys, args));
  }
