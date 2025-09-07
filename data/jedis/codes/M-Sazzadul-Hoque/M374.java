  @Override
  public Object evalsha(final byte[] sha1) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.evalsha(sha1));
  }
