  @Override
  public Object evalshaReadonly(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.evalshaReadonly(sha1, keys, args));
  }
