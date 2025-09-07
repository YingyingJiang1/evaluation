  @Override
  public List<byte[]> zrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrangeByLex(key, min, max));
  }
