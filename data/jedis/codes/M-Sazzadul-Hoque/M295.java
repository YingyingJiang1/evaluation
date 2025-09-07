  @Override
  public List<byte[]> lpop(final byte[] key, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.lpop(key, count));
  }
