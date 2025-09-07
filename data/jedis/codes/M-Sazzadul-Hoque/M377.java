  @Override
  public Object evalsha(final byte[] sha1, final int keyCount, final byte[]... params) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.evalsha(sha1, keyCount, params));
  }
