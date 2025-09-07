  @Override
  public long hstrlen(final byte[] key, final byte[] field) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hstrlen(key, field));
  }
