  @Override
  public Object evalsha(final byte[] sha1, final List<byte[]> keys, final List<byte[]> args) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.evalsha(sha1, keys, args));
  }
