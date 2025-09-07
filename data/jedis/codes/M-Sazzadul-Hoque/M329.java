  @Override
  public List<byte[]> zrevrangeByLex(final byte[] key, final byte[] max, final byte[] min) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeByLex(key, max, min));
  }
