  @Override
  public List<byte[]> rpop(final byte[] key, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.rpop(key, count));
  }
