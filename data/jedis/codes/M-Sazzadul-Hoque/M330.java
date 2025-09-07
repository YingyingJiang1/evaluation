  @Override
  public List<byte[]> zrevrangeByLex(final byte[] key, final byte[] max, final byte[] min,
      final int offset, final int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.zrevrangeByLex(key, max, min, offset, count));
  }
